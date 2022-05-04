package com.bootcamp.enterprise.passive.currentaccount.repository;

import com.bootcamp.enterprise.passive.currentaccount.entity.EnterpriseAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface EnterpriseAccountRepository extends ReactiveMongoRepository<EnterpriseAccount, String> {

    Mono<EnterpriseAccount> findByIdClient(String idClient);
}