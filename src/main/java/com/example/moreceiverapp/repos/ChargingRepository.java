package com.example.moreceiverapp.repos;

import com.example.moreceiverapp.models.ChargingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargingRepository extends JpaRepository<ChargingEntity, Long> {
    @Query(value = "SELECT * FROM tbl_charging where subscriberNumber = :subscriberNumber", nativeQuery = true)
    ChargingEntity selectBysubscriberNumber(@Param("subscriberNumber") String subscriberNumber);
    @Query(value = "SELECT count(*) AS total FROM tbl_charging WHERE origintimestamp >= CURRENT_DATE::timestamp AND origintimestamp < now()::timestamp AND charging_mechanism = 2 AND responsecode = 0", nativeQuery = true)
    Integer getDotTodaysChargedCount();
}
