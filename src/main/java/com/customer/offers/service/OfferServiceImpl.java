package com.customer.offers.service;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customer.offers.dto.OfferDto;
import com.customer.offers.entity.OfferEntity;
import com.customer.offers.entity.OfferStatus;
import com.customer.offers.exception.BusinessException;
import com.customer.offers.repository.OfferRepository;

@Service
public class OfferServiceImpl implements OfferService {

	@Autowired
	private OfferRepository offerRepository;

	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public OfferDto createOffer(OfferDto offerDto) {
		OfferEntity entity = offerRepository.save(modelMapper.map(offerDto, OfferEntity.class));
		if (entity == null) {
			throw new BusinessException(409, "Error creating the offer");
		}

		return modelMapper.map(entity, OfferDto.class);

	}

	@Override
	public OfferDto getOfferById(Long id) {
		OfferEntity entity = offerRepository.findById(id)
				.orElseThrow(() -> new BusinessException(404, "Offer not found"));
		if (!entity.isValid() || !entity.getStatus().equals(OfferStatus.ACTIVE)) {
			entity.setStatus(OfferStatus.CLOSED);
			offerRepository.save(entity);
			throw new BusinessException(409, "Invalid offer status - offer is not active");
		}
		return modelMapper.map(entity, OfferDto.class);
	}

	@Override
	public OfferDto cancelOffer(Long id) {
		OfferEntity entity = offerRepository.findById(id)
				.orElseThrow(() -> new BusinessException(404, "Offer not found"));
		if (!OfferStatus.ACTIVE.equals(entity.getStatus())) {
			throw new BusinessException(409, "Invalid offer status");
		}
		entity.setStatus(OfferStatus.CANCELLED);
		OfferEntity saved = offerRepository.save(entity);
		return modelMapper.map(saved, OfferDto.class);
	}

	@Override
	public List<OfferDto> getOffers() {
		return offerRepository.findAll().stream().filter(entity -> entity.isValid())
				.map(entity -> modelMapper.map(entity, OfferDto.class)).collect(Collectors.toList());
	}

}
