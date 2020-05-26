package com.app;

import com.app.Entities.DyidEntity;
import com.app.Entities.DyidRepository;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.hadoop.io.AvroKeyValue;
import org.apache.avro.specific.SpecificDatumReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.app.Records.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;

@SpringBootApplication
@Configuration
@Service
public class MyService {

    //    @Autowired
//    private DyidRepository repository;
//
//    public app.MyService(DyidRepository repository) {
//        this.repository = repository;
//    }
//
    public MyService() {
    }

    @Transactional
    public void doWork() throws IOException {

        Parser parser = new Parser();
        InputStream in = MyService.class.getClass().getResourceAsStream("/omnimap.avsc");
        Schema mSchema = parser.parse(in);

        SpecificDatumReader<GenericRecord> datumReader = new SpecificDatumReader<GenericRecord>(mSchema);

        DataFileReader<GenericRecord> dataFileReader = null;
        try {
            dataFileReader = new DataFileReader<GenericRecord>(new File("omnimap.avro"), datumReader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GenericRecord kv = null;
        while (dataFileReader.hasNext()) {
            kv = dataFileReader.next(kv);
//            AvroKeyValue<Long, Long> avroKeyValue = new AvroKeyValue(kv);
//            DyidEntity dyid = new DyidEntity(avroKeyValue.getKey());
//            DyidEntity leader = new DyidEntity(avroKeyValue.getValue());
//            dyid.leader(leader);
//            dyidRepository.save(dyid);
        }
    }

//
//    @Bean
//    CommandLineRunner neo4jDemo(DyidRepository dyidRepository) {
//        return args -> {
//
//            SpecificDatumReader<GenericRecord> datumReader = new SpecificDatumReader(AvroKeyValue.getSchema(Schema.create(Schema.Type.LONG), Schema.create(Schema.Type.LONG)));
//
//            DataFileReader<GenericRecord> dataFileReader = null;
//            try {
//                dataFileReader = new DataFileReader(new File("dyid_leader.avro"), datumReader);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            GenericRecord kv = null;
//            while (dataFileReader.hasNext()) {
//                kv = dataFileReader.next(kv);
//                AvroKeyValue<Long, Long> avroKeyValue = new AvroKeyValue(kv);
//                DyidEntity dyid = new DyidEntity(avroKeyValue.getKey());
//                DyidEntity leader = new DyidEntity(avroKeyValue.getValue());
//                dyid.leader(leader);
//                dyidRepository.save(dyid);
//            }
//        };
//    }

    public static void main(String[] args) throws IOException {
        MyService myService = new MyService();
        myService.doWork();
    }

//    public static void main(String[] args) {
//        SpringApplication.run(MyService.class, args);
//    }

}
