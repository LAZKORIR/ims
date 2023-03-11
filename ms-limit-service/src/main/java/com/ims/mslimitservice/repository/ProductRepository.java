package com.ims.mslimitservice.repository;

import com.ims.mslimitservice.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products,Integer> {

    Products findByProductId(String productid);

    List<Products> findByMaxLimitLessThanEqual(BigDecimal price);
}
