package com.customer.offers.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.customer.offers.dto.OfferDto;
import com.customer.offers.entity.Currency;
import com.customer.offers.entity.OfferEntity;
import com.customer.offers.entity.OfferStatus;
import com.customer.offers.exception.BusinessException;
import com.customer.offers.repository.OfferRepository;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class OfferServiceTests {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@InjectMocks
	private OfferServiceImpl offerService;

	@Mock
	private OfferRepository offerRepository;

	@Mock
	private SimpleDateFormat dateFormat;

	@BeforeAll
	public void setup() {
		this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	}

	private List<OfferDto> getMockDataDto() {
		List<OfferDto> offers = new ArrayList<>();
		try {
			OfferDto offerDto1 = new OfferDto();
			offerDto1.setId(1L);
			offerDto1.setVersion(1);
			offerDto1.setName("winter wear");
			offerDto1.setDescription("winter sale deals");
			offerDto1.setPrice(new BigDecimal(10));
			offerDto1.setCurrency(Currency.GBP);
			offerDto1.setProductId(1);
			offerDto1.setStatus(OfferStatus.ACTIVE);
			offerDto1.setExpiryDate(dateFormat.parse("2022-10-29"));
			OfferDto offerDto2 = new OfferDto();
			offerDto2.setId(2L);
			offerDto2.setVersion(1);
			offerDto2.setName("Summer wear");
			offerDto2.setDescription("Summer sale deals");
			offerDto2.setPrice(new BigDecimal(10));
			offerDto2.setProductId(1);
			offerDto2.setCurrency(Currency.INR);
			offerDto2.setStatus(OfferStatus.CANCELLED);
			offerDto2.setExpiryDate(dateFormat.parse("2022-10-29"));
			offers.add(offerDto1);
			offers.add(offerDto2);
		} catch (ParseException e) {
			logger.error("Exception occurred in parsing the date:" + e.getMessage());
		}
		return offers;
	}

	private List<OfferEntity> getMockDataEntity() {
		List<OfferEntity> entities = new ArrayList<>();
		try {
			OfferEntity offerEntity1 = new OfferEntity();
			offerEntity1.setId(1L);
			offerEntity1.setVersion(1);
			offerEntity1.setName("winter wear");
			offerEntity1.setDescription("winter sale deals");
			offerEntity1.setPrice(new BigDecimal(10));
			offerEntity1.setCurrency(Currency.GBP);
			offerEntity1.setProductId(1);
			offerEntity1.setStatus(OfferStatus.ACTIVE);
			offerEntity1.setExpiryDate(dateFormat.parse("2022-10-29"));
			OfferEntity offerEntity2 = new OfferEntity();
			offerEntity2.setId(2L);
			offerEntity2.setVersion(1);
			offerEntity2.setName("Summer wear");
			offerEntity2.setDescription("Summer sale deals");
			offerEntity2.setPrice(new BigDecimal(10));
			offerEntity2.setProductId(1);
			offerEntity2.setCurrency(Currency.INR);
			offerEntity2.setStatus(OfferStatus.CANCELLED);
			offerEntity2.setExpiryDate(dateFormat.parse("2021-10-29"));
			entities.add(offerEntity1);
			entities.add(offerEntity2);
		} catch (ParseException e) {
			logger.error("Exception occurred in parsing the date:" + e.getMessage());
		}
		return entities;
	}

	private OfferEntity getMockCancelledOffer() {
		OfferEntity cancelledOffer = getMockDataEntity().get(1);
		return cancelledOffer;
	}

	@Test
	@DisplayName("VALID - create a new offer in the application")
	public void validCreateOffer() {
		List<OfferDto> offers = this.getMockDataDto();
		OfferEntity entity = getMockDataEntity().get(0);
		when(offerRepository.save(entity)).thenReturn(entity);
		OfferDto saved = offerService.createOffer(offers.get(0));
		assertEquals(saved.getId(), offers.get(0).getId());
	}

	@Test
	@DisplayName("INVALID - Error creating a new offer in the application")
	public void validErrorCreatingOffer() {
		List<OfferDto> offers = this.getMockDataDto();
		OfferEntity entity = getMockDataEntity().get(0);
		when(offerRepository.save(entity)).thenReturn(null);
		BusinessException thrown = assertThrows(BusinessException.class, () -> {
			offerService.createOffer(offers.get(0));
		});
		assertEquals(thrown.getErrorCode(), 409);
		assertEquals(thrown.getErrorMessage(), "Error creating the offer");
	}

	@Test
	@DisplayName("VALID - get offer by id in the application")
	public void getValidOfferById() {
		Optional<OfferEntity> entity = Optional.of(getMockDataEntity().get(0));
		when(offerRepository.findById(1L)).thenReturn(entity);
		OfferDto offerDto = offerService.getOfferById(1L);
		assertEquals(offerDto.getId(), 1L);
	}

	@Test
	@DisplayName("INVALID - Error finding the offer with invalid id in the application")
	public void getOfferWithInvalidID() {
		when(offerRepository.findById(1L)).thenReturn(Optional.empty());
		BusinessException thrown = assertThrows(BusinessException.class, () -> {
			offerService.getOfferById(1L);
		});
		assertEquals(thrown.getErrorCode(), 404);
		assertEquals(thrown.getErrorMessage(), "Offer not found");
	}

	@Test
	@DisplayName("VALID - get offer by id in the application which is expired")
	public void getInValidOfferById() {
		Optional<OfferEntity> entity = Optional.of(getMockCancelledOffer());
		when(offerRepository.findById(1L))
				.thenReturn(entity);
		BusinessException thrown = assertThrows(BusinessException.class, () -> {
			offerService.getOfferById(1L);
		});
		assertEquals(thrown.getErrorCode(), 409);
		assertEquals(thrown.getErrorMessage(), "Invalid offer status - offer is not active");
	}

	@Test
	@DisplayName("VALID - CANCEL the existing offer in the application")
	public void cancelOffer() {
		Optional<OfferEntity> entity = Optional.of(getMockDataEntity().get(0));
		Optional<OfferEntity> toBeCancelled = Optional.of(getMockCancelledOffer());
		when(offerRepository.findById(1L)).thenReturn(entity);
		when(offerRepository.save(entity.get())).thenReturn(toBeCancelled.get());
		OfferDto offerDto = offerService.cancelOffer(1L);
		assertEquals(offerDto.getStatus(), OfferStatus.CANCELLED);
	}

	@Test
	@DisplayName("INVALID - CANCEL the existing offer in the application which is already cancelled")
	public void cancelOfferWithInvalidStatus1() {
		Optional<OfferEntity> toBeCancelled = Optional.of(getMockCancelledOffer());
		when(offerRepository.findById(1L)).thenReturn(toBeCancelled);
		BusinessException thrown = assertThrows(BusinessException.class, () -> {
			offerService.cancelOffer(1L);
		});
		assertEquals(thrown.getErrorCode(), 409);
		assertEquals(thrown.getErrorMessage(), "Invalid offer status");
	}
	
	@Test
	@DisplayName("INVALID - CANCEL the existing offer in the application which is already closed")
	public void cancelOfferWithInvalidStatus4() {
		Optional<OfferEntity> toBeCancelled = Optional.of(getMockCancelledOffer());
		when(offerRepository.findById(1L)).thenReturn(toBeCancelled);
		BusinessException thrown = assertThrows(BusinessException.class, () -> {
			offerService.cancelOffer(1L);
		});
		assertEquals(thrown.getErrorCode(), 409);
		assertEquals(thrown.getErrorMessage(), "Invalid offer status");
	}

	@Test
	@DisplayName("INVALID - CANCEL the existing offer in the application which is already CLOSED")
	public void cancelOfferWithInvalidStatus2() {
		Optional<OfferEntity> toBeCancelled = Optional.of(getMockCancelledOffer());
		when(offerRepository.findById(1L)).thenReturn(toBeCancelled);
		BusinessException thrown = assertThrows(BusinessException.class, () -> {
			offerService.cancelOffer(1L);
		});
		assertEquals(thrown.getErrorCode(), 409);
		assertEquals(thrown.getErrorMessage(), "Invalid offer status");
	}

	@Test
	@DisplayName("INVALID - CANCEL the offer in the application which does not exist")
	public void cancelOfferWithInvalidStatus3() {
		when(offerRepository.findById(1L)).thenThrow(new BusinessException(404, "Offer not found"));
		BusinessException thrown = assertThrows(BusinessException.class, () -> {
			offerService.cancelOffer(1L);
		});
		assertEquals(thrown.getErrorCode(), 404);
		assertEquals(thrown.getErrorMessage(), "Offer not found");
	}

	@Test
	@DisplayName("VALID - GET ALL THE OFFERS in the application")
	public void getAllOffers() {
		List<OfferEntity> offerEntities = getMockDataEntity();
		when(offerRepository.findAll()).thenReturn(offerEntities);
		List<OfferDto> offerDtos = offerService.getOffers();
		assertEquals(offerDtos.size(), 2);
	}

	@Test
	@DisplayName("VALID - GET ALL THE OFFERS in the application when there is no existing offers")
	public void getAllOffers2() {
		when(offerRepository.findAll()).thenReturn(Collections.emptyList());
		List<OfferDto> offerDtos = offerService.getOffers();
		assertEquals(offerDtos.size(), 0);
	}

}
