package com.ims.msloanrepaymentservice.repository;

import com.ims.msloanrepaymentservice.entity.Loans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loans,Integer> {

    List<Loans> findByDueDate(LocalDate localDate);
}
