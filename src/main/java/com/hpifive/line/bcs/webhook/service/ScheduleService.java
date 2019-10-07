package com.hpifive.line.bcs.webhook.service;

import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.common.DateTimeModel;
import com.hpifive.line.bcs.webhook.dao.SendMsgDao;
import com.hpifive.line.bcs.webhook.dao.SendPushJobDao;
import com.hpifive.line.bcs.webhook.entities.SendMessageEntity;
import com.hpifive.line.bcs.webhook.entities.config.SendMessageMode;
import com.hpifive.line.bcs.webhook.entities.config.SendMessageStatus;
import com.hpifive.line.bcs.webhook.exception.CustomException;
import com.hpifive.line.bcs.webhook.model.scheduler.SendMessageJob;
import com.hpifive.line.bcs.webhook.model.scheduler.SendMsgTaskExecuter;

@Component
@Service
public class ScheduleService {

	private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);
	private static final String MSG_SEND_GROUP = "MSG_SEND_GROUP";
	private static final Object SCHEDULE_FLAG = new Object();
	@Autowired
	private SendPushJobDao jobDao;
	@Autowired
	private SendMsgDao sendMsgDao;
	
	private SchedulerFactory std = new StdSchedulerFactory();
	private Scheduler scheduler;
	
	private Map<String, JobKey> onScheduleList = new HashMap<>();

	@PostConstruct
	public void init() {
		try {
			this.scheduler = std.getScheduler();
			this.scheduler.start();
		} catch (Exception e) {
			logger.error("std.getScheduler", e);
		}
	}
	
	public void addJobBySendId(Long sendId) throws CustomException {
		SendMessageEntity sendMessage = this.sendMsgDao.getSendMessageById(sendId, SendMessageStatus.CREATE);
		if (sendMessage == null) {
			throw new CustomException("Can't find send Job by id"+sendId.toString());
		}
		this.addJobBySendMessage(sendMessage);
	}
	
	public void loadFromDatabase() {
		try {
			List<SendMessageEntity> sendMessages = this.sendMsgDao.getSendListByStatus(SendMessageStatus.CREATE);
			if (sendMessages == null) {
				logger.info("No scheduling from DB");
				return;
			}
			for (SendMessageEntity sendMsg : sendMessages) {
				this.addJobBySendMessage(sendMsg);
			}
		} catch (Exception e) {
			logger.error("loadFromDB", e);
		}
	}
	@PreDestroy
	public void stopSchedule() throws SchedulerException {
		synchronized(SCHEDULE_FLAG) {
			logger.debug("Service Stop ...");
			this.scheduler.shutdown(true);
			this.onScheduleList.clear();
			this.scheduler = null;
			this.onScheduleList = null;
		}
	}
	
	private void checkSchedule() throws SchedulerException {
		if (this.scheduler == null || this.scheduler.isShutdown() ) {
			this.restartSchedule();
		}
	}
	
	private void restartSchedule() {
		logger.info("Restart Schedule ...");
			try {
				this.scheduler = this.std.getScheduler();
				this.onScheduleList = new HashMap<>();
				this.scheduler.start();
			} catch (Exception e) {
				logger.error("[Error] Restart Schedule: ", e);
			}
	}
	private void addJobBySendMessage(SendMessageEntity sendMsg) {
		if (sendMsg == null) {
			return;
		}
		Long sendId = sendMsg.getId();
		Object expression = this.getTimeExpressionBy(sendMsg);
		if (expression != null) {
			this.addPushMsgSchedule(sendId, expression);
			this.jobDao.setJobBy(sendId);
		}
	}
	
	private Object getTimeExpressionBy(SendMessageEntity sendMsg) {
		String mode = sendMsg.getMode();
		if (mode == null) {
			return null;
		}
		if (SendMessageMode.NOW.toString().equals(mode)) {
			Integer randomSecond = new SecureRandom().nextInt(10); // random 0 ~ max 10s
			return DateTimeModel.plusMillisecond(new Date(), TimeUnit.SECONDS.toMillis(randomSecond));
		} else if (SendMessageMode.ONCE.toString().equals(mode)) {
			Date startTime =  sendMsg.getSchedule();
			if (startTime.compareTo(new Date()) > 0) {
				return startTime;
			}
		} else {
			return this.parseToCronExpression(sendMsg);
		}
		return null;
	}
	
	private void addPushMsgSchedule(Long sendId, Object arg) {
		try {
	        JobDetail jobDetail = createJobDetail(sendId);
	        logger.debug("Job Detail: {}", jobDetail);
	       Trigger trigger = this.getTriggerBy(jobDetail, arg);
	       synchronized (SCHEDULE_FLAG) {
				this.checkSchedule();
				String detailName = createDetailName(sendId);
				if (! this.onScheduleList.containsKey(detailName)) {
	    			Date result = scheduler.scheduleJob(jobDetail, trigger);
	    			JobKey jobKey =  jobDetail.getKey();
	    			this.onScheduleList.put(detailName, jobKey);
	    			logger.info("addCommandSchedule result: {}", result);
	    			logger.info("addCommandSchedule detailName: {}", detailName);
				}
			}
		} catch (Exception e) {
			logger.error("Error Add PushMsgSchedule {}", e);
		}
	}
	
	private Trigger getTriggerBy(JobDetail jobDetail, Object arg) {
		try {
			if (arg instanceof Date) {
				return this.createSimpleTrigger((Date) arg, jobDetail);
			} else if (arg instanceof String) {
				return this.createCronTrigger((String) arg, jobDetail);
			}
		} catch (Exception e) {
			logger.error("Error to get Trigget By JobDetail {} , Arg {}", jobDetail, arg);
		}
		return null;
	}
	
	public void deletePushJobBy(Long sendId) {
		try {
			SendMessageEntity entity = this.sendMsgDao.getSendMessageById(sendId);
			if (entity != null) {
				String mode = entity.getMode();
				if (SendMessageMode.NOW.toString().equals(mode) || SendMessageMode.ONCE.toString().equals(mode)) {
					this.deletePushMsgSchedule(sendId);
				}
			}
		} catch (Exception e) {
			logger.error("Delete Scheduler Job Fail", e);
		}
	}


	private boolean deletePushMsgSchedule(Long msgId) throws SchedulerException{
		logger.info("deleteMsgSendSchedule: {}", msgId);

		String detailName = createDetailName(msgId);

		if(msgId != null){
			logger.info("msgId : {}", msgId);

			synchronized (SCHEDULE_FLAG) {

    			checkSchedule();
    			
				JobKey jobKey = this.onScheduleList.get(detailName);
				if(jobKey != null){
					boolean result = scheduler.deleteJob(jobKey);
	
					logger.info("deleteMsgSendSchedule Success msgId: {}", msgId);
					this.onScheduleList.remove(detailName);
					
					return result;
				}
			}
		}
		
		return false;
	}
	
	public String parseToCronExpression(SendMessageEntity entity) {
		
		String result = "0 ";
		String type = entity.getMode();
		
		/**
		 * EveryMonth 11 12:13:00
		 */
		if (SendMessageMode.MONTHLY.toString().equals(type)) {
			String day = entity.getModeDay().toString();
			String min = entity.getModeMin().toString();
			String hour = entity.getModeHour().toString();
			result = generateCronExpression("0", min, hour, day, "*", "?", "*");
		}		
		/**
		 * EveryWeek 1 12:13:00
		 */
		else if (SendMessageMode.WEEKLY.toString().equals(type)) {
			String week = entity.getModeWeek();
			String min = entity.getModeMin().toString();
			String hour = entity.getModeHour().toString();
			result = generateCronExpression("0", min, hour, "?", "*", week, "*");
		} else if (SendMessageMode.EVERYDAY.toString().equals(type)) {
			String hour = entity.getModeHour().toString();
			String min = entity.getModeMin().toString();
			result = generateCronExpression("0", min, hour, "*", "*", "?", "*");
		}
		logger.info("parseToCronExpression: {}", result );
		
		return result;
	}

	private String generateCronExpression(String sec, String min, String hour, String day, String month, String week, String year){
		return sec + " " + min + " " + hour + " " + day + " " + month + " " + week + " " + year;
	}
	
	private JobDetail createJobDetail(Long sendId){

		String detailName = createDetailName(sendId);
		String detailGroup = MSG_SEND_GROUP; 
		
		/**
		 * Setting JobDetailFactoryBean
		 */
		JobDetailFactoryBean detailFactory = new JobDetailFactoryBean();
		detailFactory.setName(detailName);
		detailFactory.setGroup(detailGroup);
		detailFactory.setBeanName(detailName);
        
		detailFactory.setJobClass(SendMessageJob.class);
        
        Map<String, Object> jobDataAsMap = new HashMap<>();
        SendMsgTaskExecuter runTask = new SendMsgTaskExecuter();
        jobDataAsMap.put("SendMsgTaskExecuter", runTask);
        jobDataAsMap.put("sendId", sendId);
        detailFactory.setJobDataAsMap(jobDataAsMap);
        
        /**
         * Create JobDetail
         */
        detailFactory.afterPropertiesSet();
        return detailFactory.getObject();   
	}
	public CronTrigger createCronTrigger(String cronExpression, JobDetail jobDetail) throws CustomException, ParseException{

        /**
         * Setting CronTriggerFactoryBean
         */
        CronTriggerFactoryBean triggerFactory = new CronTriggerFactoryBean();
        triggerFactory.setName(UUID.randomUUID().toString());
        triggerFactory.setJobDetail(jobDetail);
        try {
        	triggerFactory.setCronExpression(cronExpression);
		} catch (Exception e) { // Handle
			logger.error("create cron trigger", e);
			throw new CustomException("CronExpression Error");
		}
        /**
         * Create CronTrigger
         */
        triggerFactory.afterPropertiesSet();
        
        return triggerFactory.getObject();
	}
	private Trigger createSimpleTrigger(Date startTime, JobDetail jobDetail) throws CustomException{

        /**
         * Setting CronTriggerFactoryBean
         */
		SimpleTriggerFactoryBean triggerFactory = new SimpleTriggerFactoryBean();
        triggerFactory.setName(UUID.randomUUID().toString());
        triggerFactory.setJobDetail(jobDetail);
        try {
        	logger.debug("createSimpleTrigger: {}", startTime);
        	triggerFactory.setStartTime(startTime);
        	triggerFactory.setRepeatCount(0);
        	triggerFactory.setRepeatInterval(1000);
		} catch (Exception e) { // Handle
			logger.error("create simple trigger", e);
			throw new CustomException("CronExpression Error");
		}
        /**
         * Create SimpleTrigger
         */
        triggerFactory.afterPropertiesSet();
        
        return triggerFactory.getObject();
	}
	
	private String createDetailName(Long msgId){
		return  "MsgId:" + msgId;
	}
	
	public <T> JobDetail getJobDetailByClass(Class<T> task) {
		JobDetailFactoryBean detailFactory = new JobDetailFactoryBean();
		detailFactory.setName(task.getSimpleName()+"detailFactoryName");
		detailFactory.setGroup(task.getSimpleName()+"detailFactoryGroupName");
		detailFactory.setBeanName("detail"+task.getSimpleName());
        
		detailFactory.setJobClass(task);
		detailFactory.afterPropertiesSet();
        return detailFactory.getObject();   
	}
	
	public void schedulerJob(JobDetail jobDetail, Trigger trigger) {
		try {
			Date date = this.scheduler.scheduleJob(jobDetail, trigger);
			logger.info("Scheduler Job run in {}", date);
		} catch (SchedulerException e) {
			logger.error("SchedulerJob Fail", e);
		}
	}
	
	public <T> void createCronJob(Class<T> jobExecuter, String cronExpress) throws CustomException, ParseException {
		JobDetail jobDetail = this.getJobDetailByClass(jobExecuter);
		Trigger trigger = this.createCronTrigger(cronExpress, jobDetail);
		this.schedulerJob(jobDetail, trigger);
	}
	
}
