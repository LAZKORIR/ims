package com.ims.msloanrepaymentservice.repository;

import com.ims.msloanrepaymentservice.model.Loans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loans,Integer> {

}
