package com.example.moreceiverapp.repos;

import com.example.moreceiverapp.models.BlacklistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistRepository extends JpaRepository<BlacklistEntity, Long> {
    @Query(value = "SELECT * FROM tbl_blacklist WHERE msisdn = :msisdn and status=1", nativeQuery = true)
    BlacklistEntity selectByMsisdn(@Param("msisdn") String msisdn);

}
