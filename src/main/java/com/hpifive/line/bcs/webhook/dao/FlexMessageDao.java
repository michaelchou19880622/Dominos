package com.hpifive.line.bcs.webhook.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.common.EnumExtension;
import com.hpifive.line.bcs.webhook.common.OptionalExtension;
import com.hpifive.line.bcs.webhook.common.TemplateProducer;
import com.hpifive.line.bcs.webhook.entities.FlexMessageBoxComponentList;
import com.hpifive.line.bcs.webhook.entities.FlexMessageBoxContainerEntity;
import com.hpifive.line.bcs.webhook.entities.FlexMessageBubbleComponentList;
import com.hpifive.line.bcs.webhook.entities.FlexMessageBubbleContainerEntity;
import com.hpifive.line.bcs.webhook.entities.FlexMessageButtonComponentEntity;
import com.hpifive.line.bcs.webhook.entities.FlexMessageCarouselEntity;
import com.hpifive.line.bcs.webhook.entities.FlexMessageComponentActionEntity;
import com.hpifive.line.bcs.webhook.entities.FlexMessageEntity;
import com.hpifive.line.bcs.webhook.entities.FlexMessageIconComponentEntity;
import com.hpifive.line.bcs.webhook.entities.FlexMessageImageComponentEntity;
import com.hpifive.line.bcs.webhook.entities.FlexMessageSeparatorComponentEntity;
import com.hpifive.line.bcs.webhook.entities.FlexMessageSpacerComponentEntity;
import com.hpifive.line.bcs.webhook.entities.FlexMessageTextComponentEntity;
import com.hpifive.line.bcs.webhook.entities.config.FlexMessageBubbleTypes;
import com.hpifive.line.bcs.webhook.entities.config.FlexMessageTypes;
import com.hpifive.line.bcs.webhook.exception.DaoException;
import com.hpifive.line.bcs.webhook.repository.FlexMessageBoxComponentListRepository;
import com.hpifive.line.bcs.webhook.repository.FlexMessageBoxContainerRepository;
import com.hpifive.line.bcs.webhook.repository.FlexMessageBubbleComponentListRepository;
import com.hpifive.line.bcs.webhook.repository.FlexMessageBubbleContainerRepository;
import com.hpifive.line.bcs.webhook.repository.FlexMessageButtonComponentRepository;
import com.hpifive.line.bcs.webhook.repository.FlexMessageCarouselRepository;
import com.hpifive.line.bcs.webhook.repository.FlexMessageComponentActionRepository;
import com.hpifive.line.bcs.webhook.repository.FlexMessageIconComponentRepository;
import com.hpifive.line.bcs.webhook.repository.FlexMessageImageComponentRepository;
import com.hpifive.line.bcs.webhook.repository.FlexMessageRepository;
import com.hpifive.line.bcs.webhook.repository.FlexMessageSeparatorComponentRepository;
import com.hpifive.line.bcs.webhook.repository.FlexMessageSpacerComponentRepository;
import com.hpifive.line.bcs.webhook.repository.FlexMessageTextComponentRepository;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.FlexComponent;
import com.linecorp.bot.model.message.flex.component.Icon;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.component.Button.ButtonHeight;
import com.linecorp.bot.model.message.flex.component.Button.ButtonStyle;
import com.linecorp.bot.model.message.flex.component.Icon.IconAspectRatio;
import com.linecorp.bot.model.message.flex.component.Image.ImageAspectMode;
import com.linecorp.bot.model.message.flex.component.Image.ImageAspectRatio;
import com.linecorp.bot.model.message.flex.component.Image.ImageSize;
import com.linecorp.bot.model.message.flex.component.Separator;
import com.linecorp.bot.model.message.flex.component.Spacer;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.component.Text.TextWeight;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.container.Carousel;
import com.linecorp.bot.model.message.flex.container.FlexContainer;
import com.linecorp.bot.model.message.flex.unit.FlexAlign;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexGravity;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;

@Service
@Component
public class FlexMessageDao {
	
	private static final Logger logger = LoggerFactory.getLogger(FlexMessageDao.class);
	
	@Autowired
	private FlexMessageRepository flexMessageRepository;
	@Autowired
	private FlexMessageCarouselRepository carouselRepository;
	@Autowired
	private FlexMessageBubbleContainerRepository bubbleContainerRepository;
	@Autowired
	private FlexMessageBubbleComponentListRepository bubbleComponentListRepository;
	@Autowired
	private FlexMessageBoxContainerRepository boxContainerRepository;
	@Autowired
	private FlexMessageBoxComponentListRepository boxComponentListRepository;
	@Autowired
	private FlexMessageButtonComponentRepository buttonComponentRepository;
	@Autowired
	private FlexMessageIconComponentRepository iconComponentRepository;
	@Autowired
	private FlexMessageImageComponentRepository imageComponentRepository;
	@Autowired
	private FlexMessageTextComponentRepository textComponentRepository;
	@Autowired
	private FlexMessageComponentActionRepository componentActionRepository;
	@Autowired
	private FlexMessageSeparatorComponentRepository separatorComponentRepository;
	@Autowired
	private FlexMessageSpacerComponentRepository spacerComponentRepository;
	
	public FlexMessage getFlexMessageById(Long flexId) {
		try {
			FlexMessageEntity flexEntity = this.getFlexById(flexId);
			if (flexEntity != null) {
				String type = flexEntity.getType();
				FlexContainer container = null;
				if (FlexMessageTypes.BUBBLE.toString().equals(type)) {
					container = this.getBubbleByFlexId(flexId);
				} else if (FlexMessageTypes.CAROUSEL.toString().equals(type)) {
					container = this.getCarouselByFlexId(flexId);
				}
				return new FlexMessage(flexEntity.getAltText(), container);
			}
		} catch (Exception e) {
			logger.error(String.format("get FlexMessage By Id %d", flexId), e);
		}
		return null;
	}
	
	private Carousel getCarouselByFlexId(Long flexId) {
		List<FlexMessageCarouselEntity> list = this.carouselRepository.findByFlexIdOrderByOrderIndexAsc(flexId);
		if (list == null || list.isEmpty()) {
			return null;
		}
		try {
			List<Bubble> content = new ArrayList<>();
			for (FlexMessageCarouselEntity entity : list) {
				Bubble tempBubble = this.getBubbleByBubbleId(entity.getBubbleId());
				if (tempBubble != null) {
					content.add(tempBubble);
				}
			}
			return Carousel.builder().contents(content).build();
		} catch (Exception e) {
			logger.error(String.format("Can't get Carousel By Flex Id %d", flexId), e);
		}
		return null;
	}
	
	private Bubble getBubbleByFlexId(Long flexId) {
		try {
			FlexMessageBubbleContainerEntity container = this.bubbleContainerRepository.findByFlexId(flexId);
			return this.getBubbleByBubbleId(container.getId());
		} catch (Exception e) {
			logger.error(String.format("Can't get Bubble by FlexId %d", flexId), e);
		}
		return null;
	}
	
	private Bubble getBubbleByBubbleId(Long bubbleId) throws DaoException {
		Box header = null;
		Image hero = null;
		Box body = null;
		Box footer = null;
		try {
			FlexMessageBubbleComponentList headerList = this.bubbleComponentListRepository.findByBubbleIdAndBubbleTypeOrderByOrderIndexAsc(bubbleId, FlexMessageBubbleTypes.HEADER.toString());
			FlexMessageBubbleComponentList heroList = this.bubbleComponentListRepository.findByBubbleIdAndBubbleTypeOrderByOrderIndexAsc(bubbleId, FlexMessageBubbleTypes.HERO.toString());
			FlexMessageBubbleComponentList bodyList = this.bubbleComponentListRepository.findByBubbleIdAndBubbleTypeOrderByOrderIndexAsc(bubbleId, FlexMessageBubbleTypes.BODY.toString());
			FlexMessageBubbleComponentList footerList = this.bubbleComponentListRepository.findByBubbleIdAndBubbleTypeOrderByOrderIndexAsc(bubbleId, FlexMessageBubbleTypes.FOOTER.toString());
			if (headerList != null) {
				header = this.getBoxById(headerList.getComponentId());
			}
			if (heroList != null) {
				hero = (Image) this.getFlexComponentByIdAndType(heroList.getComponentId(), FlexMessageTypes.IMAGE.toString());
			}
			if (bodyList != null) {
				body = this.getBoxById(bodyList.getComponentId());
			}
			if (footerList != null) {
				footer = this.getBoxById(footerList.getComponentId());
			}
			return Bubble.builder().header(header).hero(hero).body(body).footer(footer).build();
		} catch (Exception e) {
			logger.error("get Bubble by bubble id", e);
			throw DaoException.message(String.format("Can't find Bubble by Bubble ID %d", bubbleId));
		}
	}
	
	private Box getBoxById(Long boxId) {
		try {
			List<FlexComponent> components = new ArrayList<>();
			FlexMessageBoxContainerEntity boxContainer = this.getBoxContainerById(boxId);
			List<FlexMessageBoxComponentList> list = this.boxComponentListRepository.findAllByBoxIdOrderByOrderIndexAsc(boxId);
			for (FlexMessageBoxComponentList comp : list) {
				FlexComponent temp = this.getFlexComponentByIdAndType(comp.getComponentId(), comp.getComponentType());
				if (temp != null) {
					components.add(temp);
				}
			}
			return Box.builder()
					.layout(EnumExtension.valueOf(FlexLayout.class, boxContainer.getLayout()))
					.flex(boxContainer.getFlex())
					.contents(components)
					.spacing(EnumExtension.valueOf(FlexMarginSize.class, boxContainer.getSpacing()))
					.margin(EnumExtension.valueOf(FlexMarginSize.class, boxContainer.getMargin())).build();
		} catch (DaoException e) {
			logger.error(String.format("getBoxById %d", boxId), e);
		}
		return null;
	}
	
	private FlexComponent getFlexComponentByIdAndType(Long componentId, String componentType) {
		if (componentId == null || componentType == null) {
			return null;
		}
		FlexComponent component = null;
		Action action = this.getActionByComponentIdAndComponentType(componentId, componentType);
		try {
			if (FlexMessageTypes.BOX.toString().equals(componentType)) {
				return this.getBoxById(componentId);
			} else if (FlexMessageTypes.BUTTON.toString().equals(componentType)) {
				FlexMessageButtonComponentEntity entity = this.getButtonComponentById(componentId);
				component = Button.builder().action(action)
						.color(entity.getColor())
						.flex(entity.getFlex())
						.gravity(EnumExtension.valueOf(FlexGravity.class, entity.getGravity()))
						.height(EnumExtension.valueOf(ButtonHeight.class, entity.getHeight()))
						.margin(EnumExtension.valueOf(FlexMarginSize.class, entity.getMargin()))
						.style(EnumExtension.valueOf(ButtonStyle.class, entity.getStyle())).build();
			}
			else if (FlexMessageTypes.TEXT.toString().equals(componentType)) {
				FlexMessageTextComponentEntity entity = this.getTextComponentById(componentId);
				component = Text.builder().flex(entity.getFlex())
						.text(entity.getText())
						.size(EnumExtension.valueOf(FlexFontSize.class, entity.getSize()))
						.align(EnumExtension.valueOf(FlexAlign.class, entity.getAlign()))
						.gravity(EnumExtension.valueOf(FlexGravity.class, entity.getGravity()))
						.color(entity.getColor())
						.weight(EnumExtension.valueOf(TextWeight.class, entity.getWeight()))
						.wrap(entity.getWrap())
						.margin(EnumExtension.valueOf(FlexMarginSize.class, entity.getMargin()))
						.action(action).build();
			} else if (FlexMessageTypes.ICON.toString().equals(componentType)) {
				FlexMessageIconComponentEntity entity = this.getIconComponentById(componentId);
				component = Icon.builder().url(entity.getUrl())
						.size(EnumExtension.valueOf(FlexFontSize.class, entity.getSize()))
						.aspectRatio(EnumExtension.valueOf(IconAspectRatio.class, entity.getAspectRatio()))
						.margin(EnumExtension.valueOf(FlexMarginSize.class, entity.getMargin()))
						.build();
			} else if (FlexMessageTypes.IMAGE.toString().equals(componentType)) {
				FlexMessageImageComponentEntity entity = this.getImageComponentById(componentId);
				component = Image.builder().flex(entity.getFlex())
						.url(entity.getUrl())
						.size(EnumExtension.valueOf(ImageSize.class, entity.getSize()))
						.aspectRatio(EnumExtension.valueOf(ImageAspectRatio.class, entity.getAspectRatio()))
						.aspectMode(EnumExtension.valueOf(ImageAspectMode.class, entity.getAspectMode()))
						.backgroundColor(entity.getBackgroundColor())
						.align(EnumExtension.valueOf(FlexAlign.class, entity.getAlign()))
						.action(action)
						.gravity(EnumExtension.valueOf(FlexGravity.class, entity.getGravity()))
						.margin(EnumExtension.valueOf(FlexMarginSize.class, entity.getMargin()))
						.build();
			} else if (FlexMessageTypes.SEPARATOR.toString().equals(componentType)) {
				FlexMessageSeparatorComponentEntity entity = this.getSeparatorComponentById(componentId);
				component = Separator.builder()
						.margin(EnumExtension.valueOf(FlexMarginSize.class, entity.getMargin()))
						.color(entity.getColor()).build();
			} else if (FlexMessageTypes.SPACER.toString().equals(componentType)) {
				FlexMessageSpacerComponentEntity entity = this.getSpacerComponentById(componentId);
				component = Spacer.builder().size(EnumExtension.valueOf(FlexMarginSize.class, entity.getSize())).build();
			}
			
		} catch (Exception e) {
			logger.error(String.format("getFlexComponentById %d AndType %s", componentId, componentType), e);
		}
		return component;
	}
	
	private FlexMessageEntity getFlexById(Long id) throws DaoException {
		Optional<FlexMessageEntity> entity = this.flexMessageRepository.findById(id);
		if (entity.isPresent()) {
			return entity.get();
		}
		throw DaoException.message(String.format("Can't find FlexMessage with id %d", id));
	}
	
	public FlexMessageBoxContainerEntity getBoxContainerById(Long id) throws DaoException {
		Optional<FlexMessageBoxContainerEntity> entity = this.boxContainerRepository.findById(id);
		if (entity.isPresent()) {
			return entity.get();
		}
		throw DaoException.message(String.format("Can't find BoxContainer with id %d", id));
	}
	
	public FlexMessageButtonComponentEntity getButtonComponentById(Long id) throws DaoException {
		Optional<FlexMessageButtonComponentEntity> entity = this.buttonComponentRepository.findById(id);
		if (entity.isPresent()) {
			return entity.get();
		}
		throw DaoException.message(String.format("Can't find ButtonComponent with id %d", id));
	}
	
	public FlexMessageSpacerComponentEntity getSpacerComponentById(Long id) {
		Optional<FlexMessageSpacerComponentEntity> entity = this.spacerComponentRepository.findById(id);
		return OptionalExtension.get(entity);
	}
	
	public FlexMessageSeparatorComponentEntity getSeparatorComponentById(Long id) {
		Optional<FlexMessageSeparatorComponentEntity> entity = this.separatorComponentRepository.findById(id);
		return OptionalExtension.get(entity);
	}
	
	public FlexMessageIconComponentEntity getIconComponentById(Long id)  {
		Optional<FlexMessageIconComponentEntity> entity = this.iconComponentRepository.findById(id);
		return OptionalExtension.get(entity);
	}
	
	public FlexMessageImageComponentEntity getImageComponentById(Long id) {
		Optional<FlexMessageImageComponentEntity> entity = this.imageComponentRepository.findById(id);
		return OptionalExtension.get(entity);
	}
	public FlexMessageTextComponentEntity getTextComponentById(Long id) {
		Optional<FlexMessageTextComponentEntity> entity = this.textComponentRepository.findById(id);
		return OptionalExtension.get(entity);
	}
	
	public Action getActionByComponentIdAndComponentType(Long id, String type) {
		List<FlexMessageComponentActionEntity> actionEntity = this.componentActionRepository.findByComponentIdAndComponentType(id, type);
		if (actionEntity == null || actionEntity.isEmpty()) {
			return null;
		}
		return TemplateProducer.action(actionEntity.get(0));
	}
	
	public List<Action> getActionsByComponentIdAndComponentType(Long id, String type) {
		List<FlexMessageComponentActionEntity> actionEntity = this.componentActionRepository.findByComponentIdAndComponentType(id, type);
		if (actionEntity == null || actionEntity.isEmpty()) {
			return null;
		}
		List<Action> result = new ArrayList<>();
		for (FlexMessageComponentActionEntity flexMessageComponentActionEntity : actionEntity) {
			Action temp = TemplateProducer.action(flexMessageComponentActionEntity);
			if (temp != null) {
				result.add(temp);
			}
		}
		return (result.isEmpty()) ? null : result;
	}
	
	
}
