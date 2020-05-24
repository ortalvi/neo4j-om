package Entities;

import lombok.Data;
import lombok.Setter;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@NodeEntity
@Data
public class FlowEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String flowName;
    private String startTime;
    @Setter
    private String endTime;

    private FlowEntity() {};

    public FlowEntity(String name) {
        this.flowName = name;
        this.startTime = LocalDate.now().toString();
    }

    @Relationship(type = "REQUIRES_JOB", direction = Relationship.UNDIRECTED)
    public Set<JobEntity> dependsOn;

    public void setDependsOn(JobEntity job) {
        if (dependsOn == null) {
            dependsOn = new HashSet<>();
        }
        dependsOn.add(job);
    }

    public String toString() {
        return this.flowName + " depends on following jobs: "
                + Optional.ofNullable(this.dependsOn).orElse(
                Collections.emptySet()).stream()
                .map(JobEntity::getJobName)
                .collect(Collectors.toList());

    }
}