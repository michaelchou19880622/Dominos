package com.hpifive.line.bcs.webhook.repository;

import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.ProductEntity;

public interface ProductRepository extends CrudRepository<ProductEntity, Long> {

}
