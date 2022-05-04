package com.bootcamp.enterprise.passive.currentaccount.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class EnterpriseAccount {

    @Id
    private String id;
    private String accountNumber;
    private String idAccountType;
    private String idCompany;
    private Double amount;
    private List<Holder> listOfHolders;
    private List<Signatory> listOfSignatories;
    private Integer operationsNumber;
    private Double minimumBalance;
    private Date createDate;
    private String createUser;
    private Date modifyDate;
    private String modifyUser;
    private short registrationStatus;
    private Date insertionDate;
    private String fk_insertionUser;
    private String insertionTerminal;

}
