package com.hpifive.line.bcs.webhook.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hpifive.line.bcs.webhook.entities.InvoiceEntity;
import com.hpifive.line.bcs.webhook.repository.InvoiceEntityRepository;
import com.hpifive.line.bcs.webhook.service.InvoiceEventService;
import com.hpifive.line.bcs.webhook.service.InvoiceRewardMatcherService;

@RestController
@RequestMapping(path="/invoice/detail/valid")
public class ValidInvoiceDetailController {

	@Autowired
	private InvoiceEntityRepository repository;
	@Autowired
	private InvoiceEventService service;
	@Autowired
	private InvoiceRewardMatcherService matchService;
	
	@RequestMapping(value="/gov/{inv}", method=RequestMethod.GET)
	public ResponseEntity<Object> validFromGovByInvoiceId(@PathVariable("inv") Long invoiceId) {
		try {
			Optional<InvoiceEntity> optional = this.repository.findById(invoiceId);
			return ResponseEntity.ok(this.service.getInvoiceFromGov(optional.get()));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e);
		}
	}
	
	@RequestMapping(value="/{inv}", method=RequestMethod.GET)
	public ResponseEntity<Object> validByInvoiceId(@PathVariable("inv") Long invoiceId) {
		try {
			Optional<InvoiceEntity> optional = this.repository.findById(invoiceId);
			this.matchService.matchInvoiceGoods(optional.get());
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e);
		}
	}
}
