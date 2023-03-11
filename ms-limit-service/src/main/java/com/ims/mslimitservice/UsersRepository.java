package com.ims.mslimitservice;

import com.ims.mslimitservice.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<UserDetails, Integer> {

    UserDetails findByMsisdn(String msisdn);

}
