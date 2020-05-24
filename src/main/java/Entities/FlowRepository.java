package Entities;

import org.springframework.data.repository.CrudRepository;

public interface FlowRepository extends CrudRepository<FlowEntity, Long> {
   FlowEntity findByFlowName(String flowName);
}
