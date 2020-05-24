package Entities;

import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface JobRepository extends CrudRepository<JobEntity, Long> {

    JobEntity findByJobName(String jobName);
    Set<JobEntity> findAll();

}