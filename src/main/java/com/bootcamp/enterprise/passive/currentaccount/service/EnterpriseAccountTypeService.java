package com.bootcamp.enterprise.passive.currentaccount.service;

import com.bootcamp.enterprise.passive.currentaccount.entity.EnterpriseAccountType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface EnterpriseAccountTypeService {

    public Flux<EnterpriseAccountType> getAll();

    public Mono<EnterpriseAccountType> getById(String id);

    public Mono<EnterpriseAccountType> save(EnterpriseAccountType enterpriseAccountType);

    public Mono<EnterpriseAccountType> update(EnterpriseAccountType enterpriseAccountType);

    public Mono<EnterpriseAccountType> delete(String id);
}