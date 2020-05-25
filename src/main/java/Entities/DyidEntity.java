package Entities;


import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
@Data
public class DyidEntity {
    @Id
    @GeneratedValue
    private Long dyid;

    public DyidEntity() {
    }

    public DyidEntity(Long dyid) {
        this.dyid = dyid;
    }

    @Relationship(type = "DYID_TO_LEADER", direction = Relationship.OUTGOING)
    public DyidEntity dyidToLeaderRelations;

    private void leader(DyidEntity dyid) {
        dyidToLeaderRelations = dyid;
    }
}