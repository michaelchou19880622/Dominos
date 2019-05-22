package com.hpifive.line.bcs.webhook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hpifive.line.bcs.webhook.entities.MessageTemplateActionEntity;

public interface MessageTemplateActionRepository extends CrudRepository<MessageTemplateActionEntity, Integer>{

	@Query(value="select distinct M from MessageTemplateActionEntity M where M.templateId = :templateId and M.templateType = :templateType")
	public List<MessageTemplateActionEntity> findByTemplateIdAndTemplateType(@Param("templateId") Integer templateId, @Param("templateType") String templateType);
}
