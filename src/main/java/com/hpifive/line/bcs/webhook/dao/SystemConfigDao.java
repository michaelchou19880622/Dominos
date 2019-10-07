package com.hpifive.line.bcs.webhook.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.common.PageExtension;
import com.hpifive.line.bcs.webhook.entities.SystemConfigEntity;
import com.hpifive.line.bcs.webhook.repository.SystemConfigRepository;

@Service
@Component
public class SystemConfigDao {

	@Autowired
	private SystemConfigRepository repository;
	
	public String getValueByKey(String key) {
		Pageable pageable = PageRequest.of(0, 1);
		Page<String> pages = this.repository.findValueByKey(key, pageable);
		return PageExtension.getFirstFromPage(pages);
	}
	
	public SystemConfigEntity getByKey(String key) {
		return this.repository.findOneByKey(key);
	}
	
	public void save(SystemConfigEntity entity) {
		this.repository.save(entity);
	}
}
