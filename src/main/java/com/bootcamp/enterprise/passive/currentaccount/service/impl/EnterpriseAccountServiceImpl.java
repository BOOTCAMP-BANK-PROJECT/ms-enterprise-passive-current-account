package com.bootcamp.enterprise.passive.currentaccount.service.impl;


import com.bootcamp.enterprise.passive.currentaccount.entity.EnterpriseAccount;
import com.bootcamp.enterprise.passive.currentaccount.entity.EnterpriseClient;
import com.bootcamp.enterprise.passive.currentaccount.entity.EnterpriseCreditCard;
import com.bootcamp.enterprise.passive.currentaccount.repository.EnterpriseAccountRepository;
import com.bootcamp.enterprise.passive.currentaccount.repository.EnterpriseAccountTypeRepository;
import com.bootcamp.enterprise.passive.currentaccount.service.EnterpriseAccountService;
import com.bootcamp.enterprise.passive.currentaccount.service.WebClientService;
import com.bootcamp.enterprise.passive.currentaccount.util.Constant;
import com.bootcamp.enterprise.passive.currentaccount.util.handler.exceptions.BadRequestException;
import com.bootcamp.enterprise.passive.currentaccount.util.handler.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class EnterpriseAccountServiceImpl implements EnterpriseAccountService {

    public final EnterpriseAccountRepository repository;

    public final EnterpriseAccountTypeRepository enterpriseAccountTypeRepository;

    public final WebClientService webClient;


    @Override
    public Flux<EnterpriseAccount> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<EnterpriseAccount> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<EnterpriseAccount> save(EnterpriseAccount enterpriseAccount) {

        return repository.findByIdClient(enterpriseAccount.getIdCompany())
                .map(sa -> {
                    throw new BadRequestException(
                            "ID",
                            "Client has one or more enterpriseAccounts",
                            sa.getId(),
                            EnterpriseAccountServiceImpl.class,
                            "save.onErrorResume"
                    );
                })
                .switchIfEmpty(
                        enterpriseAccountTypeRepository.findById(enterpriseAccount.getIdAccountType())
                                .flatMap(at ->
                                        webClient
                                                .getWebClient("entrando desde c>>" + enterpriseAccount.getIdCompany())
                                                .get()
                                                .uri("/client/enterprise/find/" + enterpriseAccount.getIdCompany())
                                                .retrieve()
                                                .bodyToMono(EnterpriseClient.class)
                                                .flatMap(c -> {

                                                    enterpriseAccount.setId(null);
                                                    enterpriseAccount.setInsertionDate(new Date());
                                                    enterpriseAccount.setRegistrationStatus((short) 1);

                                                    if (at.getAbbreviation().equals(Constant.ENTERPRISE_ACCOUNT_TYPE_VIP)) {
                                                        if (c.getProfile().equals(Constant.ENTERPRISE_CLIENT_TYPE_VIP)) {

                                                            return webClient
                                                                    .getWebClient()
                                                                    .get()
                                                                    .uri("personal/active/credit_card/" + c.getId())
                                                                    .retrieve()
                                                                    .bodyToMono(EnterpriseCreditCard.class)
                                                                    .flatMap(card -> repository.save(enterpriseAccount))
                                                                    .switchIfEmpty(Mono.error(new NotFoundException(
                                                                            "ID",
                                                                            "Client doesn't have one credit card",
                                                                            enterpriseAccount.getIdCompany(),
                                                                            EnterpriseAccountServiceImpl.class,
                                                                            "save.notFoundException"
                                                                    )));
                                                        } else {
                                                            return Mono.error(new NotFoundException(
                                                                    "ID",
                                                                    "Client is not VIP",
                                                                    enterpriseAccount.getIdCompany(),
                                                                    EnterpriseAccountServiceImpl.class,
                                                                    "save.notFoundException"
                                                            ));
                                                        }
                                                    } else {
                                                        return repository.save(enterpriseAccount);
                                                    }
                                                })

                                )
                                .switchIfEmpty(
                                        Mono.error(new NotFoundException(
                                                "ID",
                                                "EnterpriseAccount Type with id " + enterpriseAccount.getIdAccountType() + " does not exists.",
                                                enterpriseAccount.getIdCompany(),
                                                EnterpriseAccountServiceImpl.class,
                                                "save.notFoundException"
                                        )))
                )
                .onErrorResume(e -> Mono.error(e))
                .cast(EnterpriseAccount.class);
    }

    @Override
    public Mono<EnterpriseAccount> update(EnterpriseAccount enterpriseAccount) {

        return repository.findById(enterpriseAccount.getId())
                .switchIfEmpty(Mono.error(new Exception("An item with the id " + enterpriseAccount.getId() + " was not found. >> switchIfEmpty")))
                .flatMap(p -> repository.save(enterpriseAccount))
                .onErrorResume(e -> Mono.error(new BadRequestException(
                        "ID",
                        "An error occurred while trying to update an item.",
                        e.getMessage(),
                        EnterpriseAccountServiceImpl.class,
                        "update.onErrorResume"
                )));
    }

    @Override
    public Mono<EnterpriseAccount> delete(String id) {
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
                        EnterpriseAccountServiceImpl.class,
                        "update.onErrorResume"
                )));
    }
}