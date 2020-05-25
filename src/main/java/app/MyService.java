package app;

import Entities.DyidEntity;
import Entities.DyidRepository;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.hadoop.io.AvroKeyValue;
import org.apache.avro.specific.SpecificDatumReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
@EnableNeo4jRepositories
public class MyService {

    //    @Autowired
//    private DyidRepository repository;
//
//    public app.MyService(DyidRepository repository) {
//        this.repository = repository;
//    }
//
//    public app.MyService() {
//    }
//
//
    @Bean
    CommandLineRunner neo4jDemo(DyidRepository dyidRepository) {
        return args -> {

            SpecificDatumReader<GenericRecord> datumReader = new SpecificDatumReader(AvroKeyValue.getSchema(Schema.create(Schema.Type.LONG), Schema.create(Schema.Type.LONG)));

            DataFileReader<GenericRecord> dataFileReader = null;
            try {
                dataFileReader = new DataFileReader(new File("dyid_leader.avro"), datumReader);
            } catch (IOException e) {
                e.printStackTrace();
            }

            GenericRecord kv = null;
            while (dataFileReader.hasNext()) {
                kv = dataFileReader.next(kv);
                AvroKeyValue<Long, Long> avroKeyValue = new AvroKeyValue(kv);
                DyidEntity dyid = new DyidEntity(avroKeyValue.getKey());
                DyidEntity leader = new DyidEntity(avroKeyValue.getValue());
                dyid.leader(leader);
                dyidRepository.save(dyid);
            }
        };
    }
//
//    public static void main(String[] args) throws IOException {
//        app.MyService myService = new app.MyService();
//        myService.doWork();
//    }

    public static void main(String[] args) {
        SpringApplication.run(MyService.class, args);

    }

}