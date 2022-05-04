package com.bootcamp.enterprise.passive.currentaccount.service;

import com.bootcamp.enterprise.passive.currentaccount.entity.EnterpriseAccount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface EnterpriseAccountService {

    public Flux<EnterpriseAccount> getAll();

    public Mono<EnterpriseAccount> getById(String id);

    public Mono<EnterpriseAccount> save(EnterpriseAccount enterpriseAccount);

    public Mono<EnterpriseAccount> update(EnterpriseAccount enterpriseAccount);

    public Mono<EnterpriseAccount> delete(String id);
}