package com.app.Entities;


import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
@Data
public class DyidEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String dyid;

    private DyidEntity() {
    }

    public DyidEntity(String dyid) {
        this.dyid = dyid;
    }

    @Relationship(type = "DYID_TO_LEADER", direction = Relationship.OUTGOING)
    public DyidEntity dyidToLeaderRelations;

    public void leader(DyidEntity dyid) {
        dyidToLeaderRelations = dyid;
    }


    @Relationship(type = "DYID_TO_CUID", direction = Relationship.OUTGOING)
    public Set<CuidEntity> dyidToCuidRelations;

    public void setDyidToCuidEdge(CuidEntity cuid) {
        if (dyidToCuidRelations == null) {
            dyidToCuidRelations = new HashSet<>();
        }
        dyidToCuidRelations.add(cuid);
    }

    @Relationship(type = "DYID_TO_DYID", direction = Relationship.DIRECTION)
    public Set<DyidEntity> dyidToDyidRelations;

    public void setDyidToDyidEdge(DyidEntity dyid) {
        if (dyidToDyidRelations == null) {
            dyidToDyidRelations = new HashSet<>();
        }
        dyidToDyidRelations.add(dyid);
    }
}
