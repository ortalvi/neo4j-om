package com.app.Entities;

import org.springframework.data.repository.CrudRepository;

public interface CuidRepository extends CrudRepository<CuidEntity, Long> {
    CuidEntity findByCuidAndType(String cuid, String type);
}
