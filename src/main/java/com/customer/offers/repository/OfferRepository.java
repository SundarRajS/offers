package com.customer.offers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.customer.offers.entity.OfferEntity;

@Repository
public interface OfferRepository extends JpaRepository<OfferEntity, Long> {
}
