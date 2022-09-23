package com.customer.offers.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import lombok.Data;

@Data
@MappedSuperclass
public class BaseEntity<T> {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	@SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 1, initialValue = 1)
	protected Long id;

	@Version
	@Column
	protected int version;

	@Column(name = "created_dt")
	protected Date createdDate;

	@Column(name = "updated_dt")
	protected Date updatedDate;

	public void prePersist() {
		this.updatedDate = this.createdDate = new Date();
	}

	public void preUpdate() {
		this.updatedDate = new Date();
	}
}
