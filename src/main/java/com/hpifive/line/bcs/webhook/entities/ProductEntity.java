package com.hpifive.line.bcs.webhook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="product")
public class ProductEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="image_url")
	private String imageUrl;
	
	@Column(name="thumbnail_image_url")
	private String thumbnailImageUrl;
	
	@Column(name="description")
	private String description;
	
	@Column(name="instruction")
	private String instruction;
	
	@Column(name="memo")
	private String memo;
	
	@Column(name="memo_url")
	private String memoUrl;

	public ProductEntity() {
	}

	public ProductEntity(Long id, String name, String imageUrl, String thumbnailImageUrl, String description,
			String instruction, String memo, String memoUrl) {
		super();
		this.id = id;
		this.name = name;
		this.imageUrl = imageUrl;
		this.thumbnailImageUrl = thumbnailImageUrl;
		this.description = description;
		this.instruction = instruction;
		this.memo = memo;
		this.memoUrl = memoUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getThumbnailImageUrl() {
		return thumbnailImageUrl;
	}

	public void setThumbnailImageUrl(String thumbnailImageUrl) {
		this.thumbnailImageUrl = thumbnailImageUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemoUrl() {
		return memoUrl;
	}

	public void setMemoUrl(String memoUrl) {
		this.memoUrl = memoUrl;
	}
	
	
	
}
