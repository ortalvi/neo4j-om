package Entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
@Data
public class SectionEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Getter
    @Setter
    private Integer sectionId;
    @Getter
    @Setter
    private String sectionName;

    private SectionEntity() {}
    public SectionEntity(Integer sectionId) {
        this.sectionId = sectionId;
    }
    public SectionEntity(String name) {
        this.sectionName = name;
    }
    public SectionEntity(Integer sectionId, String sectionName) {
        this.sectionId = sectionId;
        this.sectionName = sectionName;
    }

    @Relationship(type = "WORKS_ON", direction = Relationship.UNDIRECTED)
    public Set<JobEntity> jobs;
}
