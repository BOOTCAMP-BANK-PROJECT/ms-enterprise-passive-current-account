package com.bootcamp.enterprise.passive.currentaccount.repository;

import com.bootcamp.enterprise.passive.currentaccount.entity.EnterpriseAccountType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnterpriseAccountTypeRepository extends ReactiveMongoRepository<EnterpriseAccountType, String> {

}