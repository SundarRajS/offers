package com.customer.offers.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.customer.offers.entity.Currency;
import com.customer.offers.entity.OfferStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.KebabCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonNaming(value = KebabCaseStrategy.class)
public class OfferDto extends BaseDto<Long> {

	@NotBlank(message = "error.client.name")
	private String name;

	private String description;

	@NotNull(message = "price cannot be null")
	private BigDecimal price;

	@NotNull(message = "currency cannot be null")
	private Currency currency;

	@NotNull(message = "Product Id cannot be null")
	private int productId;

	@NotNull(message = "offer status cannot be null")
	private OfferStatus status;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date expiryDate;

}
