package com.app;

import com.app.Records.DyidEdge;
import com.app.Records.KeyValue;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Configuration
@Service
public class MyService {

    public MyService() {
    }

    @Transactional
    public void doWork() throws IOException {
        List<DyidEdge> dyidEdges = readFromJson();

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

    private List<DyidEdge> readFromJson() throws FileNotFoundException {
        List<DyidEdge> result = new ArrayList<>();
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader("om.json"));

        JsonObject obj = gson.fromJson(reader, JsonObject.class);
        JsonArray jsonArray = obj.get("list").getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            JsonElement key = jsonElement.getAsJsonObject().get("key");
            DyidEdge dyidEdge = gson.fromJson(key, DyidEdge.class);
            result.add(dyidEdge);
        }

        return result;
    }

}
