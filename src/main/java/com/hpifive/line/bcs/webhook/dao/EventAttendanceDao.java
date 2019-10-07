package com.hpifive.line.bcs.webhook.dao;

import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.common.CriteriaQueryDto;
import com.hpifive.line.bcs.webhook.common.DateTimeModel;
import com.hpifive.line.bcs.webhook.common.ListExtension;
import com.hpifive.line.bcs.webhook.common.OptionalExtension;
import com.hpifive.line.bcs.webhook.controller.body.KeyValueBody;
import com.hpifive.line.bcs.webhook.entities.EventAttendanceAttributeEntity;
import com.hpifive.line.bcs.webhook.entities.EventAttendanceEntity;
import com.hpifive.line.bcs.webhook.entities.EventPrizeDetailEntity;
import com.hpifive.line.bcs.webhook.entities.LineUserEntity;
import com.hpifive.line.bcs.webhook.entities.config.EventApplyStatus;
import com.hpifive.line.bcs.webhook.entities.config.LineUserBindStatus;
import com.hpifive.line.bcs.webhook.repository.EventAttendanceRepository;

@Service
@Component
public class EventAttendanceDao {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private UserLinkDao userLinkDao;
	
	@Autowired
	private EventAttendanceRepository eventAttendanceRepository;
	
	public boolean isUserConfirmExpired(Long eventId, Long userId) {
		Pageable pageable = PageRequest.of(0, 1);
		ZonedDateTime currentTime = DateTimeModel.getTime();
		List<Long> result = this.eventAttendanceRepository.findExpiredUserIdByEventIdAndUserIdAndTime(eventId, userId, currentTime, pageable);
		return (result != null && ! result.isEmpty());
	}
	
	public List<Long> getExpiredUserIdByEventIdAndApplyStatus(Long eventId, Integer applyStatus) {
		ZonedDateTime currentTime = DateTimeModel.getTime();
		return this.eventAttendanceRepository.findExpiredUserIdByApplyStatusAndTime(eventId, applyStatus, currentTime);
	}
	
	public <T extends Object> List<Long> getNonPrizeAndNonBlockUserIdByAttr(List<KeyValueBody<T>> filters, Long eventId, Long prizeId, Integer applyStatus, Integer userStatus, Integer volume) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQueryDto<Long> dto = this.getNonBlockUserIdByAttr(filters, eventId, applyStatus, Arrays.asList(userStatus), volume);
		CriteriaQuery<Long> query = dto.getQuery();
		Root<EventAttendanceEntity> root = query.from(EventAttendanceEntity.class);
		Predicate[] predicates = {
				dto.getPredicate(),
				root.get("userId").in(this.getNonPrizeUserSubQueryBy(builder, query, prizeId)).not()
		};
		query.where(builder.and(predicates));
		List<Long> resultList = em.createQuery(query).getResultList();
		return this.getRandom(resultList, volume);
	}
	
	private Subquery<Long> getNonPrizeUserSubQueryBy(CriteriaBuilder builder, CriteriaQuery<Long> query, Long prizeId) {
		Subquery<Long> subQuery = query.subquery(Long.class);
		Root<EventPrizeDetailEntity> subLineUserRoot = subQuery.from(EventPrizeDetailEntity.class);
		subQuery.select(subLineUserRoot.get("userId"));
		Predicate[] predicates = {
				builder.equal(subLineUserRoot.get("eventPrizeId"), prizeId),
				subLineUserRoot.get("userId").isNotNull()
		};
		subQuery.where(builder.and(predicates));
		return subQuery;
	}
	
	public <T extends Object> List<Long> getNonBlockUserIdByAttr(List<KeyValueBody<T>> filters, Long eventId, Integer applyStatus, Integer userStatus, Integer volume) {
		CriteriaQueryDto<Long> dto = this.getNonBlockUserIdByAttr(filters, eventId, applyStatus, Arrays.asList(userStatus), volume);
		CriteriaQuery<Long> query = dto.getQuery();
		query.where(dto.getPredicate());
		List<Long> resultList = em.createQuery(query).getResultList();
		return this.getRandom(resultList, volume);
	}
	
	public <T extends Object> CriteriaQueryDto<Long> getNonBlockUserIdByAttr(List<KeyValueBody<T>> filters, Long eventId, Integer applyStatus, List<Integer> userStatus, Integer volume) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<EventAttendanceEntity> root = query.from(EventAttendanceEntity.class);
		Subquery<Long> nonBlockUserSubQuery = this.getNonBlockUserSubQueryBy(builder, query, userStatus);
		Subquery<Long> filterSubQuery = this.getFilterQueryBy(builder, query, filters, eventId);
		Predicate whereClause = builder.and(builder.equal(root.get("eventId"), eventId), builder.equal(root.get("applyStatus"), applyStatus));
		query.select(root.get("userId"));
		whereClause = builder.and(whereClause, builder.in(root.get("userId")).value(nonBlockUserSubQuery));
		whereClause = builder.and(whereClause, builder.in(root.get("userId")).value(filterSubQuery));
		return new CriteriaQueryDto<>(query, whereClause);
	}
	
	public <T> List<T> getRandom(List<T> source, Integer numberOfYouWant) {
	    if (numberOfYouWant == null) {
	    	return source;
	    }
	    List<T> randomRow = new ArrayList<>();
	    List<T> copy = new ArrayList<>(source);
	    SecureRandom rand = new SecureRandom();
	    for (int i = 0; i < Math.min(numberOfYouWant, source.size()); i++) {
	    	randomRow.add( copy.remove( rand.nextInt( copy.size() )));
	    }

	    return randomRow;
	}
	
	public Subquery<Long> getNonBlockUserSubQueryBy(CriteriaBuilder builder, CriteriaQuery<Long> query, List<Integer> userStatus) {
		Subquery<Long> nonBlockUserSubQuery = query.subquery(Long.class);
		Root<LineUserEntity> subLineUserRoot = nonBlockUserSubQuery.from(LineUserEntity.class);
		nonBlockUserSubQuery.select(subLineUserRoot.get("id"));
		if (userStatus != null && userStatus.size() == 1) {
			nonBlockUserSubQuery.where(builder.equal(subLineUserRoot.get("status"), userStatus.get(0)));
		} else if (userStatus != null && userStatus.size() > 1) {
			nonBlockUserSubQuery.where(builder.in(subLineUserRoot.get("status")).value(userStatus));
		}
		return nonBlockUserSubQuery;
	}
	public <T extends Object> Subquery<Long> getFilterQueryBy(CriteriaBuilder builder, CriteriaQuery<Long> query, List<KeyValueBody<T>> filters, Long eventId) {
		Subquery<Long> subQuery = query.subquery(Long.class);
		Root<EventAttendanceAttributeEntity> root = subQuery.from(EventAttendanceAttributeEntity.class);
		Predicate whereClause = null;
		Long filterSize = filters == null ? 0L : filters.size();
		for(int i=0; i<filterSize; i+=1) {
			KeyValueBody<T> body = filters.get(i);
			Predicate keyPredicate = builder.equal(root.get("key"), body.getKey());
			Predicate valuePredicate = builder.equal(root.get("value"), body.getValue());
			Predicate tempPredicate = builder.and(keyPredicate, valuePredicate);
			if (whereClause == null) {
				whereClause = builder.and(tempPredicate);
			} else {
				whereClause = builder.or(whereClause, tempPredicate);
			}
		}
		if (whereClause != null) {
			subQuery.where(builder.and(builder.equal(root.get("eventId"), eventId), whereClause));
		}
		subQuery.select(root.get("lineUserId"));
		subQuery.groupBy(root.get("lineUserId"));
		subQuery.having(builder.greaterThanOrEqualTo(builder.count(root.get("lineUserId")), filterSize));
		return subQuery;
	}
	
	public List<Long> getUserIdByIds(List<Long> ids) {
		List<Long> result = new ArrayList<>();
		if (ids == null || ids.isEmpty()) {
			return result;
		}
		return this.eventAttendanceRepository.findUserIdByIds(ids);
	}
	
	public EventAttendanceEntity getById(Long id) {
		Optional<EventAttendanceEntity> opEntity = this.eventAttendanceRepository.findById(id);
		return OptionalExtension.get(opEntity);
	}
	
	public void deleteById(Long id) {
		this.eventAttendanceRepository.deleteById(id);
	}
	
	public void setApplyStatusByEventIdAndUserIds(Long eventId, List<Long> userIds, EventApplyStatus status) {
		this.setApplyStatusByEventIdAndUserIds(eventId, userIds, status.getValue());
	}
	
	public void setApplyStatusByEventIdAndUserIds(Long eventId, List<Long> userIds, Integer status) {
		this.eventAttendanceRepository.updateApplyStatusByEventIdAndUserIds(eventId, userIds, status);
	}
	
	public void setApplyStatusByIds(List<Long> ids, EventApplyStatus status) {
		if (ids == null || ids.isEmpty()) {
			return;
		}
		this.eventAttendanceRepository.updateApplyStatusAndApplyDateByIds(ids, status.getValue(), DateTimeModel.getTime());
	}
	
	public void setApplyStatusAndDateById(Long id, EventApplyStatus status, ZonedDateTime date) {
		this.eventAttendanceRepository.updateApplyStatusAndApplyDateById(id, status.getValue(), date);
	}
	
	public void addByEventIdAndUserId(Long userId, Long eventId) {
		EventAttendanceEntity entity = new EventAttendanceEntity();
		entity.setEventId(eventId);
		entity.setUserId(userId);
		entity.setApplyStatus(1);
		entity.setInputErrorCount(0);
		this.save(entity);
	}
	
	public void incrementErrorCountById(Long id) {
		this.eventAttendanceRepository.incrementInputErrorCountById(id);
	}
	
	public void incrementErrorCountByUserIdAndEventId(Long userId, Long eventId) {
		this.eventAttendanceRepository.incrementInputErrorCountByUserIdAndEventId(userId, eventId);
	}
	
	public void setErrorCountAndApplyStatusById(Long id, Integer count, Integer applyStatus) {
		EventAttendanceEntity entity = this.getById(id);
		if (entity != null) {
			entity.setInputErrorCount(count);
			entity.setApplyStatus(applyStatus);
			this.eventAttendanceRepository.save(entity);
		}
	}
	
	public void save(EventAttendanceEntity entity) {
		this.eventAttendanceRepository.save(entity);
	}
	
	public EventAttendanceEntity getByUserIdAndEventId(Long userId, Long eventId) {
		Pageable pageable = PageRequest.of(0, 1); //第0頁 每頁1個
		List<EventAttendanceEntity> pages = this.eventAttendanceRepository.findByUserIdAndEventId(userId, eventId, pageable);
		return ListExtension.first(pages);
	}
	
	public EventAttendanceEntity getByUserIdAndEventIdAndUserStatus(Long eventId, Long userId, LineUserBindStatus status) {
		Pageable pageable = PageRequest.of(0, 1); //第0頁 每頁1個
		List<String> listStatus = Arrays.asList(status.toString(), LineUserBindStatus.ALL.toString());
		List<EventAttendanceEntity> pages = this.eventAttendanceRepository.findByUserIdAndEventIdAndUserStatusAndDate(userId, eventId, listStatus, new Date(), pageable);
		return ListExtension.first(pages);
	}
	
	public EventAttendanceEntity getUnFinishRegisterFlowAttendance(Long userId) {
		LineUserBindStatus status = this.userLinkDao.getStatusByUserId(userId);
		return this.getUnFinishRegisterFlowAttendance(userId, status);
	}
	
	public EventAttendanceEntity getUnFinishRegisterFlowAttendance(Long userId, LineUserBindStatus status) {
		Pageable pageable = PageRequest.of(0, 1); //第0頁 每頁1個
		List<Integer> applyStatus = Arrays.asList(EventApplyStatus.APPLY_COMPLETED_YES.getValue(), 
				EventApplyStatus.APPLY_COMPLETED.getValue(), 
				EventApplyStatus.WAIT_CONFIRM.getValue(),
				EventApplyStatus.CONFIRM.getValue(),
				EventApplyStatus.REJECT.getValue());
		List<String> listStatus = Arrays.asList(status.toString(), LineUserBindStatus.ALL.toString());
		List<EventAttendanceEntity> pages = this.eventAttendanceRepository.findByUserIdAndUserStatusAndDateAndNotInApplyStatus(userId, listStatus, new Date(), applyStatus, pageable);
		return ListExtension.first(pages);
	}
	
	public EventAttendanceEntity getByUidAndUserStatus(String uid, LineUserBindStatus status) {
		Pageable pageable = PageRequest.of(0, 1); //第0頁 每頁1個
		List<String> listStatus = Arrays.asList(status.toString(), LineUserBindStatus.ALL.toString());
		List<EventAttendanceEntity> pages = this.eventAttendanceRepository.findByUidAndUserStatusAndDate(uid, listStatus, new Date(), pageable);
		return ListExtension.first(pages);
	}
	
	public void setInputErrorCountBy(Long eventId, Long userId, Integer count) {
		this.eventAttendanceRepository.updateInputErrorCountByEventIdAndUserId(eventId, userId, count);
	}
	
}
