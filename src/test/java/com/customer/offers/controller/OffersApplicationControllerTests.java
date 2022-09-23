package com.customer.offers.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.customer.offers.dto.OfferDto;
import com.customer.offers.entity.Currency;
import com.customer.offers.entity.OfferStatus;
import com.customer.offers.exception.BusinessException;
import com.customer.offers.service.OfferService;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class OffersApplicationControllerTests {

	@InjectMocks
	private OfferController controller;

	@Mock
	private OfferService offerService;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private List<OfferDto> getMockData() {
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
			e.printStackTrace();
		}
		return offers;
	}

	@Test
	@DisplayName("VALID - GET All the offers responses from the application")
	public void testGetOffers() throws Exception {
		List<OfferDto> testOffers = getMockData();
		when(offerService.getOffers()).thenReturn(testOffers);
		ResponseEntity<List<OfferDto>> responseEntity = controller.getOffers();
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
		List<OfferDto> offers = responseEntity.getBody();
		assertEquals(offers.size(), 2);
		assertEquals(offers, testOffers);
	}

	@Test
	@DisplayName("VALID - GET ALL when there is no offers in the application")
	public void testNoOffers() throws Exception {
		when(offerService.getOffers()).thenReturn(Collections.emptyList());
		ResponseEntity<List<OfferDto>> responseEntity = controller.getOffers();
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
		List<OfferDto> offers = responseEntity.getBody();
		assertEquals(offers.size(), 0);
	}

	@Test
	@DisplayName("VALID - Add a new offer in the application")
	public void testAddNewOffer() throws Exception {
		List<OfferDto> offers = getMockData();
		when(offerService.createOffer(offers.get(0))).thenReturn(offers.get(0));
		ResponseEntity<OfferDto> responseEntity = controller.createOffer(offers.get(0));
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
		OfferDto offerDto = responseEntity.getBody();
		assertNotNull(offerDto.getId());
		assertNotNull(offerDto.getStatus());
	}

	@Test
	@DisplayName("VALID - GET the offer by Id in the application")
	public void getOfferById() throws Exception {
		List<OfferDto> offers = getMockData();
		when(offerService.getOfferById(1L)).thenReturn(offers.get(0));
		OfferDto offerById = controller.getOfferById(1L);
		assertEquals(offerById.getId(), 1L);
	}

	@Test
	@DisplayName("INVALID - GET the offer by Id in the application which does not exist")
	public void getOfferByIdInvalid() throws Exception {
		when(offerService.getOfferById(100L)).thenThrow(new BusinessException(404, "Offer not found"));
		BusinessException thrown = assertThrows(BusinessException.class, () -> {
			controller.getOfferById(100L);
		});
		assertEquals(thrown.getErrorCode(), 404);
		assertEquals(thrown.getErrorMessage(), "Offer not found");
	}

	@Test
	@DisplayName("VALID - cancel a offer in the application")
	public void cancelOffer() throws Exception {
		List<OfferDto> offers = getMockData();
		when(offerService.cancelOffer(2L)).thenReturn(offers.get(1));
		OfferDto offerById = controller.cancelOffer(2L);
		assertEquals(offerById.getId(), 2L);
		assertEquals(offerById.getStatus(), OfferStatus.CANCELLED);
	}

	@Test
	@DisplayName("INVALID - cancel a offer in the application which does not exist")
	public void cancelOfferWhichDoesNotExist() throws Exception {
		when(offerService.cancelOffer(100L)).thenThrow(new BusinessException(404, "Offer not found"));
		BusinessException thrown = assertThrows(BusinessException.class, () -> {
			controller.cancelOffer(100L);
		});
		assertEquals(thrown.getErrorCode(), 404);
		assertEquals(thrown.getErrorMessage(), "Offer not found");
	}

	@Test
	@DisplayName("INVALID - cancel a offer in the application which does not exist")
	public void cancelOfferNotActive() throws Exception {
		when(offerService.cancelOffer(1L)).thenThrow(new BusinessException(409, "Invalid offer status"));
		BusinessException thrown = assertThrows(BusinessException.class, () -> {
			controller.cancelOffer(1L);
		});
		assertEquals(thrown.getErrorCode(), 409);
		assertEquals(thrown.getErrorMessage(), "Invalid offer status");
	}

}
