package Entities;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NodeEntity
@Data
public class DateEntity {
    @Id @GeneratedValue
    private Long id;

    @Getter
    @Setter
    private String date;

    private DateEntity() {};
    public DateEntity(String time) {
        this.date = time;
    }

    @Relationship(type = "NOT_SUBMITTED", direction = Relationship.UNDIRECTED)
    public List<JobEntity> notSubmittedRelations;

    @Relationship(type = "RUNNING", direction = Relationship.UNDIRECTED)
    public List<JobEntity> runningRelations;

    public void connectAllJobsNotSubmitted(Set<JobEntity> jobs) {
        if (notSubmittedRelations == null) {
            notSubmittedRelations = new ArrayList<>();
        }
        notSubmittedRelations.addAll(jobs);
    }

    @Transactional
    public void setJobStatusRunning(JobEntity job) {
        if (runningRelations == null) {
            runningRelations = new ArrayList<>();
        }
//        if (notSubmittedRelations.contains(job))
//        notSubmittedRelations.remove(job);
        removeJob(notSubmittedRelations, job);
        runningRelations.add(job);
    }


    public boolean removeJob (List<JobEntity> jobs, JobEntity job) {

        for(JobEntity entity : jobs) {
            if (job.getJobName().equals(entity.getJobName()))
            {
                jobs.remove(entity);
                return true;
            }
        }

        return false;
    }
    public String toString() {
        return "Jobs date " + date;

    }
}
