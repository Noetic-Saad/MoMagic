package com.example.moreceiverapp.repos;

import com.example.moreceiverapp.models.TodayChargedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface TodayChargedMsisdnRepository extends JpaRepository<TodayChargedEntity,Long> {
//    @Query(value = "SELECT count(*) FROM today_tbl_billing WHERE subscribernumber = :msisdn "
//            , nativeQuery = true)
//    Integer findMsisdnInTodayTblBilling(String msisdn);
    @Query(value = "SELECT * FROM today_tbl_billing WHERE subscribernumber = :msisdn order by origintimestamp desc LIMIT 1", nativeQuery = true)
    TodayChargedEntity findbyMsisdn(String msisdn);

    Optional<?> findBySubscriberNumber(String subscriberNumber);

    @Query(value = "SELECT COUNT(*) FROM today_tbl_billing WHERE subscribernumber = :subscriberNumber", nativeQuery = true)
    Integer findMsisdn(@Param("subscriberNumber") String subscriberNumber);
    @Modifying
    @Transactional
    @Query(value = "UPDATE public.today_tbl_billing  SET   responsecode=:responsecode , attempt=attempt + 1 WHERE id=:id", nativeQuery = true)
    void updateRecordById(@Param("id") Long id, @Param("responsecode") Integer responsecode);
}