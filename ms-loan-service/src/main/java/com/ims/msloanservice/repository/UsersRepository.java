package com.ims.msloanservice.repository;

import com.ims.msloanservice.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<UserDetails, Integer> {

    UserDetails findByMsisdn(String msisdn);

}
