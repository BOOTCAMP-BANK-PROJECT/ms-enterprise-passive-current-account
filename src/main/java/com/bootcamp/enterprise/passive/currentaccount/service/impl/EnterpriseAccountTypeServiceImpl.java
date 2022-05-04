package com.bootcamp.enterprise.passive.currentaccount.service.impl;

import com.bootcamp.enterprise.passive.currentaccount.entity.EnterpriseAccountType;
import com.bootcamp.enterprise.passive.currentaccount.repository.EnterpriseAccountTypeRepository;
import com.bootcamp.enterprise.passive.currentaccount.service.EnterpriseAccountTypeService;
import com.bootcamp.enterprise.passive.currentaccount.util.Constant;
import com.bootcamp.enterprise.passive.currentaccount.util.handler.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class EnterpriseAccountTypeServiceImpl implements EnterpriseAccountTypeService {

    public final EnterpriseAccountTypeRepository repository;

    @Override
    public Flux<EnterpriseAccountType> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<EnterpriseAccountType> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<EnterpriseAccountType> save(EnterpriseAccountType enterpriseAccountType) {
        return repository.findById(enterpriseAccountType.getId())
                .map(sa -> {
                    throw new BadRequestException(
                            "ID",
                            "Client have one ore more enterpriseAccountTypes",
                            sa.getId(),
                            EnterpriseAccountTypeService.class,
                            "save.onErrorResume"
                    );
                })
                .switchIfEmpty(Mono.defer(() -> {

                            enterpriseAccountType.setId(null);
                            enterpriseAccountType.setInsertionDate(new Date());
                            enterpriseAccountType.setRegistrationStatus((short) 1);

                            return repository.save(enterpriseAccountType);
                        }
                ))
                .onErrorResume(e -> Mono.error(e)).cast(EnterpriseAccountType.class);
    }

    @Override
    public Mono<EnterpriseAccountType> update(EnterpriseAccountType enterpriseAccountType) {

        return repository.findById(enterpriseAccountType.getId())
                .switchIfEmpty(Mono.error(new Exception("An item with the id " + enterpriseAccountType.getId() + " was not found. >> switchIfEmpty")))
                .flatMap(p -> repository.save(enterpriseAccountType))
                .onErrorResume(e -> Mono.error(new BadRequestException(
                        "ID",
                        "An error occurred while trying to update an item.",
                        e.getMessage(),
                        EnterpriseAccountTypeService.class,
                        "update.onErrorResume"
                )));
    }

    @Override
    public Mono<EnterpriseAccountType> delete(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new Exception("An item with the id " + id + " was not found. >> switchIfEmpty")))
                .flatMap(p -> {
                    p.setRegistrationStatus(Constant.STATUS_INACTIVE);
                    return repository.save(p);
                })
                .onErrorResume(e -> Mono.error(new BadRequestException(
                        "ID",
                        "An error occurred while trying to delete an item.",
                        e.getMessage(),
                        EnterpriseAccountTypeService.class,
                        "update.onErrorResume"
                )));
    }
}