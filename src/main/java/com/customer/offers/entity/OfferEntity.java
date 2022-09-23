package com.customer.offers.entity;

import lombok.Data;
import java.util.Date;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "offers")
@EqualsAndHashCode(callSuper = true)
public class OfferEntity extends BaseEntity<Long> {

	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@Column(name = "description", length = 200)
	private String description;

	@Column(name = "price", nullable = false)
	private BigDecimal price;

	@Column(name = "currency", length = 5, nullable = false)
	@Enumerated(EnumType.STRING)
	private Currency currency;

	@Column(name = "product_id", nullable = false)
	private int productId;

	@Column(name = "status", length = 15, nullable = false)
	@Enumerated(EnumType.STRING)
	private OfferStatus status;

	@Column(name = "expiry_date")
	private Date expiryDate;

	public boolean isValid() {
		return expiryDate == null ? true : expiryDate.after(new Date());
	}

	@PrePersist
	public void prePersist() {
		super.prePersist();
	}

	@PreUpdate
	public void preUpdate() {
		super.preUpdate();
	}

}
