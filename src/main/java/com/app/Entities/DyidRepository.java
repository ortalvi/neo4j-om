package com.app.Entities;

import org.springframework.data.repository.CrudRepository;

public interface DyidRepository extends CrudRepository<DyidEntity, Long> {
    DyidEntity findByDyId(Long id);
}