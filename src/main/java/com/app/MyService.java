package com.app;

import com.app.Entities.*;
import com.app.Records.OmniEdge;
import com.app.Records.OmniNode;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Configuration
@Service
public class MyService {

    public MyService() {
    }

//    @Transactional
//    public void doWork() throws IOException {
//    }


//    @Bean
//    CommandLineRunner neo4jDemo(DyidRepository dyidRepository) {
//        return args -> {
//
//            dyidRepository.deleteAll();
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

    @Bean
    CommandLineRunner neo4jDemo(DyidRepository dyidRepository, CuidRepository cuidRepository) {
        return args -> {

        dyidRepository.deleteAll();
        cuidRepository.deleteAll();

        List<OmniEdge> omniEdges = readFromJson();
        omniEdges.forEach(edge -> {
            OmniNode node1 = edge.getU();
            OmniNode node2 = edge.getV();

            if (null == node1.getType()) {
                DyidEntity dyidEntity1 = dyidRepository.findByDyid(node1.getN()) == null ? new DyidEntity(node1.getN()) :
                        dyidRepository.findByDyid(node1.getN());
                if (null == node2.getType()) {
                    DyidEntity dyidEntity2 = dyidRepository.findByDyid(node2.getN()) == null ? new DyidEntity(node2.getN()) :
                            dyidRepository.findByDyid(node2.getN());
                    dyidEntity1.setDyidToDyidEdge(dyidEntity2);
                    dyidRepository.save(dyidEntity2);
                } else {
                    CuidEntity cuidEntity2 = cuidRepository.findByCuidAndType(node2.getN(), node2.getType().getTypeString()) == null ? new CuidEntity(node2.getN(), node2.getType().getTypeString()) :
                            cuidRepository.findByCuidAndType(node2.getN(), node2.getType().getTypeString());
                    dyidEntity1.setDyidToCuidEdge(cuidEntity2);
                    cuidRepository.save(cuidEntity2);
                }
                dyidRepository.save(dyidEntity1);
            } else {
                CuidEntity cuidEntity1 = cuidRepository.findByCuidAndType(node1.getN(), node1.getType().getTypeString()) == null ? new CuidEntity(node1.getN(), node1.getType().getTypeString()) :
                        cuidRepository.findByCuidAndType(node1.getN(), node1.getType().getTypeString());
                if (null == node2.getType()) {
                    DyidEntity dyidEntity2 = dyidRepository.findByDyid(node2.getN()) == null ? new DyidEntity(node2.getN()) :
                            dyidRepository.findByDyid(node2.getN());
                    cuidEntity1.setCuidToDyidEdge(dyidEntity2);
                    dyidRepository.save(dyidEntity2);
                } else {
                    CuidEntity cuidEntity2 = cuidRepository.findByCuidAndType(node2.getN(), node2.getType().getTypeString()) == null ? new CuidEntity(node2.getN(), node2.getType().getTypeString()) :
                            cuidRepository.findByCuidAndType(node2.getN(), node2.getType().getTypeString());
                    cuidEntity1.setCuidToCuidEdge(cuidEntity2);
                    cuidRepository.save(cuidEntity2);
                }
                cuidRepository.save(cuidEntity1);
            }
        });

        };
    }

//    public static void main(String[] args) throws IOException {
//        MyService myService = new MyService();
//        myService.doWork();
//    }

    public static void main(String[] args) {
        SpringApplication.run(MyService.class, args);
    }

    private List<OmniEdge> readFromJson() throws FileNotFoundException {
        List<OmniEdge> result = new ArrayList<>();
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader("om.json"));

        JsonObject obj = gson.fromJson(reader, JsonObject.class);
        JsonArray jsonArray = obj.get("list").getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            JsonElement key = jsonElement.getAsJsonObject().get("key");
            OmniEdge omniEdge = gson.fromJson(key, OmniEdge.class);
            result.add(omniEdge);
        }

        return result;
    }

}
