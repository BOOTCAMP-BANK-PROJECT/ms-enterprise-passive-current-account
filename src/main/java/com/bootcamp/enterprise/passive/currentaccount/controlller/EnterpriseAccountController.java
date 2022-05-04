package com.bootcamp.enterprise.passive.currentaccount.controlller;

import com.bootcamp.enterprise.passive.currentaccount.entity.EnterpriseAccount;
import com.bootcamp.enterprise.passive.currentaccount.service.impl.EnterpriseAccountServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("personal/passive/saving_account")
@Tag(name = "Personal Passive Product Saving EnterpriseAccount", description = "Manage Personal Passive Product saving accounts type")
@CrossOrigin(value = {"*"})
@RequiredArgsConstructor
public class EnterpriseAccountController {

    public final EnterpriseAccountServiceImpl service;

    @GetMapping
    public Mono<ResponseEntity<Flux<EnterpriseAccount>>> getAll() {
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(service.getAll())
        );
    }

    @PostMapping
    public Mono<ResponseEntity<EnterpriseAccount>> create(@RequestBody EnterpriseAccount account) {

        return service.save(account).map(p -> ResponseEntity
                .created(URI.create("/EnterpriseAccount/".concat(p.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(p)
        );
    }

    @PutMapping
    public Mono<ResponseEntity<EnterpriseAccount>> update(@RequestBody EnterpriseAccount account) {
        return service.update(account)
                .map(p -> ResponseEntity.created(URI.create("/EnterpriseAccount/"
                                .concat(p.getId())
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public Mono<ResponseEntity<EnterpriseAccount>> delete(@RequestBody String id) {
        return service.delete(id)
                .map(p -> ResponseEntity.created(URI.create("/EnterpriseAccount/"
                                .concat(p.getId())
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
