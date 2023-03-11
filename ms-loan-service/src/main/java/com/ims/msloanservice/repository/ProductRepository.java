package com.ims.msloanservice.repository;

import com.ims.msloanservice.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository  extends JpaRepository<Products,Integer> {

    Products findByProductId(String productid);
}
