package com.bootcamp.enterprise.passive.currentaccount.controlller;

import com.bootcamp.enterprise.passive.currentaccount.entity.EnterpriseAccountType;
import com.bootcamp.enterprise.passive.currentaccount.service.EnterpriseAccountTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("personal/passive/saving_enterpriseAccountType")
@Tag(name = "Personal Passive Product Saving EnterpriseAccountType Type", description = "Manage Personal Passive Product saving enterpriseAccountTypes type")
@CrossOrigin(value = {"*"})
@RequiredArgsConstructor
public class EnterpriseAccountTypeController {

    public final EnterpriseAccountTypeService service;

    @GetMapping//(value = "/fully")
    public Mono<ResponseEntity<Flux<EnterpriseAccountType>>> getAll() {
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(service.getAll())
        );
    }

    @PostMapping
    public Mono<ResponseEntity<EnterpriseAccountType>> create(@RequestBody EnterpriseAccountType enterpriseAccountType) {

        return service.save(enterpriseAccountType).map(p -> ResponseEntity
                .created(URI.create("/EnterpriseAccountType/".concat(p.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(p)
        );
    }

    @PutMapping
    public Mono<ResponseEntity<EnterpriseAccountType>> update(@RequestBody EnterpriseAccountType enterpriseAccountType) {
        return service.update(enterpriseAccountType)
                .map(p -> ResponseEntity.created(URI.create("/EnterpriseAccountType/"
                                .concat(p.getId())
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public Mono<ResponseEntity<EnterpriseAccountType>> delete(@RequestBody String id) {
        return service.delete(id)
                .map(p -> ResponseEntity.created(URI.create("/EnterpriseAccountType/"
                                .concat(p.getId())
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
