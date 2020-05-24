import com.example.demo.Entities.*;
import com.example.demo.Models.JobModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@SpringBootApplication
public class DemoApplication {

	private static final Type JOB_TYPE = new TypeToken<List<JobModel>>() {}.getType();
	private static final String FILE_PATH = "/Users/davidshpilfoygel/Downloads/data2.json";
	private static final String JOB_NAME = "BlockJob";



	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

	}
	@Bean
	CommandLineRunner neo4jDemo(JobRepository jobRepository, DateRepository dateRepository, FlowRepository flowRepository) {
		return args -> {

			flowRepository.deleteAll();
			jobRepository.deleteAll();
			dateRepository.deleteAll();

//			SectionEntity section1 = new SectionEntity();

			List<JobModel> jobModels = readFromJson();

			generateJobNodes(jobRepository, flowRepository, jobModels);
			String time = initAllJobsStatus(jobRepository, dateRepository);
			changeJobStatus(jobRepository,dateRepository);

			Set<JobEntity> all = jobRepository.findAll();
			DateEntity byDate = dateRepository.findByDate(time);

		};
	}


	private void changeJobStatus(JobRepository jRepo, DateRepository dateRepo) {
		JobEntity job = jRepo.findByJobName(JOB_NAME);
		DateEntity toDayNode = dateRepo.findByDate(LocalDate.now().toString());
		toDayNode.setJobStatusRunning(job);
		dateRepo.save(toDayNode);
		dateRepo.removeJobFromNonSubmitted(LocalDate.now().toString(), "BlockJob");

	}

	private String initAllJobsStatus(JobRepository jobRepository, DateRepository dateRepository) {
		String time = LocalDate.now().toString();
		DateEntity firstDate = new DateEntity(time);
		Set<JobEntity> all = jobRepository.findAll();
		firstDate.connectAllJobsNotSubmitted(all);
		dateRepository.save(firstDate);
		return time;
	}

	private void generateJobNodes(JobRepository jobRepository, FlowRepository flowRepository,List<JobModel> jobModels) {
		for (JobModel job:jobModels) {
			if (job.getName().toLowerCase().contains("flow")) {
				setFlowEntityWithDependencies(jobRepository, flowRepository, job);
			}
			else {
				setJobEntityWithDependencies(jobRepository, job);
			}
		}
	}

	private void setFlowEntityWithDependencies(JobRepository jobRepository, FlowRepository flowRepository, JobModel job) {
		FlowEntity flowEntity = flowRepository.findByFlowName(job.getName()) == null ? new FlowEntity(job.getName()) :
				flowRepository.findByFlowName(job.getName());
		for (String dependJob: job.getDependencies()) {
			JobEntity depends = jobRepository.findByJobName(dependJob) == null ? new JobEntity(dependJob)
					: jobRepository.findByJobName(dependJob);

			depends = jobRepository.save(depends);
			flowEntity.setDependsOn(depends);
			flowRepository.save(flowEntity);
		}
	}

	private void setJobEntityWithDependencies(JobRepository jobRepository, JobModel job) {
		JobEntity jobEntity = jobRepository.findByJobName(job.getName()) == null ? new JobEntity(job.getName()) :
				jobRepository.findByJobName(job.getName());


		for (String dependJob: job.getDependencies()) {
		JobEntity depends = jobRepository.findByJobName(dependJob) == null ? new JobEntity(dependJob)
				: jobRepository.findByJobName(dependJob);

		depends = jobRepository.save(depends);
		jobEntity.setDependsOn(depends);
	}
		jobRepository.save(jobEntity);
	}

	private List<JobModel> readFromJson() throws FileNotFoundException {
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new FileReader(FILE_PATH));
		 return gson.fromJson(reader, JOB_TYPE);
	}
}
