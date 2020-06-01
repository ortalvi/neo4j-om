package com.app.Entities;


import lombok.Data;
import org.neo4j.ogm.annotation.*;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
@Data
public class CuidEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String cuid;

    private String type;

    private CuidEntity() {};

    public CuidEntity(String cuid, String type) {
        this.cuid = cuid;
        this.type = type;
    }

    @Relationship(type = "CUID_TO_DYID", direction = Relationship.OUTGOING)
    public Set<DyidEntity> cuidToDyidRelations;

    public void setCuidToDyidEdge(DyidEntity dyid) {
        if (cuidToDyidRelations == null) {
            cuidToDyidRelations = new HashSet<>();
        }
        cuidToDyidRelations.add(dyid);
    }

    @Relationship(type = "CUID_TO_CUID", direction = Relationship.DIRECTION)
    public Set<CuidEntity> cuidToCuidRelations;

    public void setCuidToCuidEdge(CuidEntity cuid) {
        if (cuidToCuidRelations == null) {
            cuidToCuidRelations = new HashSet<>();
        }
        cuidToCuidRelations.add(cuid);
    }
}
