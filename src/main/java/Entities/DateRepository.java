package Entities;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DateRepository extends CrudRepository<DateEntity, Long> {
    DateEntity findByDate(String date);

    @Query("match (d :DateEntity)-[r:NOT_SUBMITTED]-(j:JobEntity) where d.date = $date and j.jobName = $jobName delete r;")
    DateEntity removeJobFromNonSubmitted(@Param("date") String date, @Param("jobName") String jobName);
}