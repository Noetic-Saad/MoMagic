package com.example.moreceiverapp.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Table(name = "tbl_blacklist")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BlacklistEntity {
    //DB Attributes
    @Id
    private Long id;
    private String msisdn;
    private Timestamp cdate;
    private Timestamp modify_date;
    private Integer userid;
    private Integer status;
    private String mdate;
    private Integer statuscode;
}