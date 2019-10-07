package com.hpifive.line.bcs.webhook.common;

import java.util.ArrayList;
import java.util.List;

import com.hpifive.line.bcs.webhook.entities.FlexMessageComponentActionEntity;
import com.hpifive.line.bcs.webhook.entities.MessageCarouselActionEntity;
import com.hpifive.line.bcs.webhook.entities.MessageCarouselColumnEntity;
import com.hpifive.line.bcs.webhook.entities.MessageCarouselTemplateEntity;
import com.hpifive.line.bcs.webhook.entities.MessageImagemapActionEntity;
import com.hpifive.line.bcs.webhook.entities.MessageImagemapEntity;
import com.hpifive.line.bcs.webhook.entities.MessageTemplateActionEntity;
import com.hpifive.line.bcs.webhook.entities.MessageTemplateEntity;
import com.hpifive.line.bcs.webhook.entities.config.MessageActionTypes;
import com.hpifive.line.bcs.webhook.entities.config.MessageTemplateTypes;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.ImagemapMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.imagemap.ImagemapAction;
import com.linecorp.bot.model.message.imagemap.ImagemapArea;
import com.linecorp.bot.model.message.imagemap.ImagemapBaseSize;
import com.linecorp.bot.model.message.imagemap.URIImagemapAction;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.message.template.Template;

public class TemplateProducer {

	private TemplateProducer() {}
	
	public static Message imagemap(MessageImagemapEntity m, List<ImagemapAction> actions) {
		ImagemapBaseSize baseSize = new ImagemapBaseSize(m.getBaseSizeHeight(), m.getBaseSizeWidth());
		return new ImagemapMessage(m.getBaseUrl(), m.getAltText(), baseSize, actions);
	}
	
	public static CarouselColumn carouselColumn(MessageCarouselColumnEntity col, List<Action> actions) {
		return new CarouselColumn(col.getThumbnailImageUrl(), col.getTitle(), col.getText(), actions);
	}
	
	public static Message carouselTemplate(MessageCarouselTemplateEntity c, List<CarouselColumn> columns) {
		CarouselTemplate template = new CarouselTemplate(columns);
		return new TemplateMessage(c.getAltText(), template);
	}
	
	public static Message template(MessageTemplateEntity t, List<Action> actions) {
		Template template = null;
		String type = t.getType();
		if ( type.equals(MessageTemplateTypes.BUTTONS.toString()) ) {
			template = new ButtonsTemplate(t.getThumbnailImageUrl(), t.getTitle(), t.getText(), actions);
		} else if (type.equals(MessageTemplateTypes.CONFIRM.toString())) {
			template =  new ConfirmTemplate(t.getText(), actions);
		}
		if (template != null) {
			return new TemplateMessage(t.getAltText(), template);
		}
		return null;
	}
	public static List<Action> carouselAction(List<MessageCarouselActionEntity> actions) {
		List<Action> resultActions = new ArrayList<>();
		for (MessageCarouselActionEntity action : actions) {
			Action tempA = TemplateProducer.action(action);
			if (tempA != null) {
				resultActions.add(tempA);
			}
		}
		return resultActions;
	}
	
	public static List<Action> action(List<MessageTemplateActionEntity> actions) {
		List<Action> resultActions = new ArrayList<>();
		for (MessageTemplateActionEntity action : actions) {
			Action tempA = TemplateProducer.action(action);
			if (tempA != null) {
				resultActions.add(tempA);
			}
		}
		return resultActions;
	}
	public static Action action(MessageCarouselActionEntity entity) {
		return getActionBy(entity.getType(), entity.getLabel(), entity.getText(), entity.getUri(), entity.getData());
	}
	
	public static Action action(MessageTemplateActionEntity entity) {
		return getActionBy(entity.getType(), entity.getLabel(), entity.getText(), entity.getUri(), entity.getData());
	}
	
	public static Action getActionBy(String type, String label, String text, String uri, String data) {
		if (type.equals(MessageActionTypes.POSTBACK.toString())) {
			return new PostbackAction(label, data);
		} else if (type.equals(MessageActionTypes.MESSAGE.toString())) {
			return new MessageAction(label, text);
		} else if (type.equals(MessageActionTypes.URI.toString())) {
			return new URIAction(label, uri);
		}
		return null;
	}
	
	public static Action action(FlexMessageComponentActionEntity entity) {
		return getActionBy(entity.getType(), entity.getLabel(), entity.getText(), entity.getUri(), entity.getData());
	}
	
	public static List<ImagemapAction> imagemapAction(List<MessageImagemapActionEntity> actions) {
		List<ImagemapAction> resultActions = new ArrayList<>();
		for (MessageImagemapActionEntity action : actions) {
			ImagemapAction tempA = TemplateProducer.imagemapAction(action);
			if (tempA != null) {
				resultActions.add(tempA);
			}
		}
		return resultActions;
	}
	
	public static ImagemapAction imagemapAction(MessageImagemapActionEntity entity) {
		try {
			String type = entity.getType();
			ImagemapArea area = null;
			area = new ImagemapArea(entity.getAreaX(), entity.getAreaY(), entity.getAreaWidth(), entity.getAreaHeight());
			if (type.equals(MessageActionTypes.MESSAGE.toString())) {
				return new com.linecorp.bot.model.message.imagemap.MessageImagemapAction(entity.getText(), area);
			} else if (type.equals(MessageActionTypes.URI.toString())) {
				return new URIImagemapAction(entity.getLinkUri(), area);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
}
