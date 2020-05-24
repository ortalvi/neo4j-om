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
public class JobEntity {
    @Id @GeneratedValue
    private Long id;
    private String jobName;
    private String startTime;
    @Setter
    private String endTime;

    private JobEntity() {};

    public JobEntity(String name) {
        this.jobName = name;
        this.startTime = LocalDate.now().toString();
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String name) {
        this.jobName = name;
    }

    @Relationship(type = "DEPENDS_ON", direction = Relationship.DIRECTION)
    public Set<JobEntity> dependsOn;

    public void setDependsOn(JobEntity job) {
        if (dependsOn == null) {
            dependsOn = new HashSet<>();
        }
        dependsOn.add(job);
    }

    public String toString() {
        return this.jobName + " depends on following jobs: "
                + Optional.ofNullable(this.dependsOn).orElse(
                Collections.emptySet()).stream()
                .map(JobEntity::getJobName)
                .collect(Collectors.toList());

    }
}
