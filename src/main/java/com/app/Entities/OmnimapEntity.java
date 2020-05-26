package com.app.Entities;


import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
@Data
public class OmnimapEntity {
    @Id
    @GeneratedValue
    private Long id;

    private Long omniId;

    private String type;

    public OmnimapEntity() {
    }

    public OmnimapEntity(Long omniId, String type) {
        this.omniId = omniId;
        this.type = type;
    }

    @Relationship(type = "EDGE", direction = Relationship.OUTGOING)
    public OmnimapEntity omniRelations;

    public void createEdge(OmnimapEntity omniVertex) {
        omniRelations = omniVertex;
    }
}
