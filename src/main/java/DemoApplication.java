import Entities.DyidEntity;
import Entities.DyidRepository;
import com.example.demo.Entities.*;
import com.example.demo.Models.JobModel;
import com.google.gson.reflect.TypeToken;
import org.apache.avro.file.DataFileReader;
import org.neo4j.driver.internal.shaded.reactor.util.function.Tuple2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Type;
import java.util.List;


@SpringBootApplication
public class DemoApplication {

//    private static final Type JOB_TYPE = new TypeToken<List<JobModel>>() {
//    }.getType();
    private static final String FILE_PATH = "/Users/davidshpilfoygel/Downloads/data2.json";
    private static final String JOB_NAME = "BlockJob";


    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

    }

    @Bean
    CommandLineRunner neo4jDemo(DyidRepository dyidRepository) {
        return args -> {

            dyidRepository.deleteAll();

            //Read avro
            DataFileReader<Tuple2<Long, Long>> dataFileReader = new DataFileReader<Tuple2<Long, Long>>(file, userDatumReader);

        };
    }

}
