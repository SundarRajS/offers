package com.customer.offers.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.customer.offers.dto.OfferDto;
import com.customer.offers.service.OfferService;

@RestController
@RequestMapping(produces = "application/json")
public class OfferController {

	@Autowired
	private OfferService offerService;

	@GetMapping(path = "/offers")
	public ResponseEntity<List<OfferDto>> getOffers() {
		List<OfferDto> offers = this.offerService.getOffers();
		return new ResponseEntity<List<OfferDto>>(offers, HttpStatus.OK);
	}

	@PostMapping(path = "/offers", consumes = "application/json")
	public ResponseEntity<OfferDto> createOffer(@Valid @RequestBody OfferDto offerDTO) {
		OfferDto createdOffer = offerService.createOffer(offerDTO);
		return new ResponseEntity<OfferDto>(createdOffer, HttpStatus.CREATED);
	}

	@GetMapping(path = "/offers/{id}")
	public OfferDto getOfferById(@PathVariable("id") long offerId) {
		return offerService.getOfferById(offerId);
	}

	@PostMapping(path = "offers/{id}/cancel")
	public OfferDto cancelOffer(@PathVariable("id") Long offerId) {
		return offerService.cancelOffer(offerId);
	}

}
