package com.ims.msloanservice.repository;

import com.ims.msloanservice.entity.Loans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loans,Integer> {

}
