package com.app.Entities;

import org.springframework.data.repository.CrudRepository;

public interface OmnimapRepository extends CrudRepository<DyidEntity, Long> {
    DyidEntity findByDyid(Long dyid);
}
