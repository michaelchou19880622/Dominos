package com.hpifive.line.bcs.webhook.service;

import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hpifive.line.bcs.webhook.common.DateTimeModel;
import com.hpifive.line.bcs.webhook.config.DefaultConfig;
import com.hpifive.line.bcs.webhook.entities.EventGoodsEntity;
import com.hpifive.line.bcs.webhook.entities.EventRetailerEntity;
import com.hpifive.line.bcs.webhook.entities.EventRewardCardEntity;
import com.hpifive.line.bcs.webhook.entities.GoodsDetailEntity;
import com.hpifive.line.bcs.webhook.entities.InvoiceDetailEntity;
import com.hpifive.line.bcs.webhook.entities.InvoiceEntity;
import com.hpifive.line.bcs.webhook.entities.LineUserRewardEntity;
import com.hpifive.line.bcs.webhook.entities.LineUserRewardPointEntity;
import com.hpifive.line.bcs.webhook.entities.RetailerDetailEntity;
import com.hpifive.line.bcs.webhook.entities.config.EventTypes;
import com.hpifive.line.bcs.webhook.entities.config.InvoiceStatus;
import com.hpifive.line.bcs.webhook.entities.config.RewardPointOperation;
import com.hpifive.line.bcs.webhook.invoice.InvoiceMatchBody;
import com.hpifive.line.bcs.webhook.repository.EventGoodsRepository;
import com.hpifive.line.bcs.webhook.repository.EventRetailerRepository;
import com.hpifive.line.bcs.webhook.repository.EventRewardCardRepository;
import com.hpifive.line.bcs.webhook.repository.GoodsDetailRepository;
import com.hpifive.line.bcs.webhook.repository.InvoiceEntityRepository;
import com.hpifive.line.bcs.webhook.repository.LineUserRewardPointRepository;
import com.hpifive.line.bcs.webhook.repository.LineUserRewardRepository;
import com.hpifive.line.bcs.webhook.repository.RetailerDetailRepository;

@Service
@Component
public class InvoiceRewardMatcherService {
	
	private static final Logger logger = LoggerFactory.getLogger(InvoiceRewardMatcherService.class);
	
	@Autowired
	private EventService eventService;
	@Autowired
	private InvoiceEntityRepository invoiceRepository;
	@Autowired
	private LineUserRewardPointRepository pointRepository;
	@Autowired
	private LineUserRewardRepository userRewardRepository;
	@Autowired
	private EventRewardCardRepository eventRewardCardRepository;
	@Autowired
	private EventGoodsRepository eventGoodsRepository;
	@Autowired
	private GoodsDetailRepository goodsDetailRepository;
	@Autowired
	private EventRetailerRepository eventRetailerRepository;
	@Autowired
	private RetailerDetailRepository retailerDetailRepository;
	
	private void incrementErrorCount(InvoiceEntity inv) {
		Integer errorCount = inv.getErrorCount() == null ? 0 : inv.getErrorCount();
		inv.setStatus((errorCount >= DefaultConfig.INVOICE_ERROR_COUNT.getValue()) ? InvoiceStatus.INVOICE_FAKE : InvoiceStatus.NOT_FOUND);
		inv.setErrorCount(errorCount+1);
		this.invoiceRepository.save(inv);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void matchInvoiceGoods(InvoiceEntity entity) {
		if (entity == null) {
			return;
		}
		Long eventId = entity.getEventId();
		Long userId = entity.getLineUserId();
		List<InvoiceDetailEntity> detailEntities = entity.getInvoiceDetail();
		if (detailEntities == null || detailEntities.isEmpty()) {
			this.incrementErrorCount(entity);
			return;
		}
		if (! this.isCompanyAllowed(eventId, entity.getSellerBan())) {
			entity.setStatus(InvoiceStatus.UNQUALIFIED);
			this.invoiceRepository.save(entity);
			return;
		}
		
		InvoiceMatchBody matchResult = this.matchInvoiceGoods(eventId, detailEntities);
		Integer minimizeSpend = this.eventService.getMinimizeSpend(eventId);
		if (matchResult.getTotalSpend() >= minimizeSpend && matchResult.getPoint() > 0) {
			entity.setStatus(InvoiceStatus.MATCH);
		} else {
			entity.setStatus(InvoiceStatus.NOT_MATCH);
		}
		this.invoiceRepository.save(entity);
		if (entity.getStatus() ==InvoiceStatus.MATCH.toString()) {
			logger.info("EventId {} UserId {} 配點成功 {}", entity.getEventId(), entity.getLineUserId(), matchResult.getPoint());
			this.addPoint(entity.getId(), eventId, userId, matchResult.getPoint());
		}
	}
	
	public InvoiceMatchBody matchInvoiceGoods(Long eventId, List<InvoiceDetailEntity> details) {
		Integer totalSpend = 0;
		Integer point = 0;
		try {
			for (InvoiceDetailEntity invoiceDetailEntity : details) {
				String productName = invoiceDetailEntity.getDescription().toLowerCase();
				Long goodId = this.getGoodsIdByDetailName(eventId, productName);
				EventGoodsEntity opGoodsEntity = this.eventGoodsRepository.findByEventIdAndGoodId(eventId, goodId);
				if (opGoodsEntity == null) {
					continue;
				}
				Integer amount = invoiceDetailEntity.getAmount() == null ? 0 : invoiceDetailEntity.getAmount().intValue();
				Integer tempPoint = this.getEventPointBy(eventId, goodId, invoiceDetailEntity.getQuantity());
				if (tempPoint > 0) {
					logger.info("temp Point {}", tempPoint);
					point+=tempPoint;
					totalSpend+=amount;
				}
			}
		}catch (Exception e) {
			logger.error("match Invoice Goods", e);
		}
		return new InvoiceMatchBody(point, totalSpend);
	}
	
	/**
	 * 
	 * @param productName //Invoice Detail Name.toLowercase()
	 * @return Goods ID
	 */
	private Long getGoodsIdByDetailName(Long eventId, String productName) {
		List<GoodsDetailEntity> goods = this.goodsDetailRepository.findByEventId(eventId);
		for (GoodsDetailEntity item : goods) {
			String name = item.getName().toLowerCase();
			if (productName.contains(name)) {
				logger.info("產品mapped {}", name);
				return item.getGoodId();
			}
		}
		logger.info("產品not mapped eventId{} productName {}", eventId, productName);
		return null;
	}
	
	public Integer getEventPointBy(Long eventId, Long goodId, Integer quantity) {
		logger.info("eventId {} goodId {}", eventId, goodId);
		EventGoodsEntity goodsEntity = this.eventGoodsRepository.findByEventIdAndGoodId(eventId, goodId);
		if (goodsEntity != null) {
			logger.info("Goods Entity point {}", goodsEntity.getRewardPoint() );
			quantity = quantity == null ? 0 : quantity;
			return (goodsEntity.getRewardPoint()*quantity);
		}
		return 0;
	}
	
	@Transactional
	public void addPoint(Long invoiceId, Long eventId, Long userId, Integer point) {
		if (point == null || point <= 0) {
			return;
		}
		Pageable pageable = PageRequest.of(0, 1);
		List<EventRewardCardEntity> eventCards = this.eventRewardCardRepository.findByEventId(eventId, pageable);
		if (eventCards == null || eventCards.isEmpty()) {
			return;
		}
		ZonedDateTime currentTime = DateTimeModel.getTime();
		Long cardId = eventCards.get(0).getCardId();
		LineUserRewardEntity rewardEntity = this.getUserRewardEntityBy(cardId, eventId, userId);
		rewardEntity.setPoint(rewardEntity.getPoint()+point);
		this.userRewardRepository.save(rewardEntity);
		LineUserRewardPointEntity pointEntity = new LineUserRewardPointEntity(null, rewardEntity.getId(), point, EventTypes.INVOICE.toString(), invoiceId, RewardPointOperation.ADD.toString(), currentTime);
		this.pointRepository.save(pointEntity);
	}
	
	public boolean isCompanyAllowed(Long eventId, String companyId) {
		Pageable pageable = PageRequest.of(0, 1);
		List<RetailerDetailEntity> retailerDetailEntities = this.retailerDetailRepository.findByCompanyId(companyId, pageable);
		if (retailerDetailEntities != null && ! retailerDetailEntities.isEmpty()) {
			List<EventRetailerEntity> retailers = this.eventRetailerRepository.findByEventIdAndRetailerId(eventId, retailerDetailEntities.get(0).getRetailerId(), pageable);
			return (retailers != null && ! retailers.isEmpty());
		}
		return false;
	}
	
	public LineUserRewardEntity getUserRewardEntityBy(Long cardId, Long eventId, Long userId) {
		LineUserRewardEntity rewardEntity = this.userRewardRepository.findByEventIdAndUserIdAndCardId(eventId, userId, cardId);
		if (rewardEntity == null) {
			rewardEntity = new LineUserRewardEntity(null, cardId, userId, eventId, 0, DateTimeModel.getTime(), 0, null, null);
			this.userRewardRepository.save(rewardEntity);
		}
		return rewardEntity;
	}
}
