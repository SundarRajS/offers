package com.customer.offers.service;

import java.util.List;

import com.customer.offers.dto.OfferDto;

public interface OfferService {

	public OfferDto createOffer(OfferDto offerDTO);

	public OfferDto getOfferById(Long id);

	public OfferDto cancelOffer(Long id);

	public List<OfferDto> getOffers();
}
