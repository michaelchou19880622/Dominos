package com.hpifive.line.bcs.webhook.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.common.OptionalExtension;
import com.hpifive.line.bcs.webhook.entities.ProductEntity;
import com.hpifive.line.bcs.webhook.repository.ProductRepository;

@Service
@Component
public class ProductDao {

	@Autowired
	private ProductRepository repository;

	public ProductEntity getById(Long id) {
		return OptionalExtension.get(this.repository.findById(id));
	}
	
	
}
