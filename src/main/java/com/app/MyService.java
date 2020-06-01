package com.app;

import com.app.Entities.CuidEntity;
import com.app.Entities.CuidRepository;
import com.app.Entities.DyidEntity;
import com.app.Entities.DyidRepository;
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

    @Bean
    CommandLineRunner neo4jDemo(DyidRepository dyidRepository, CuidRepository cuidRepository) {
        return args -> {

            dyidRepository.deleteAll();
            cuidRepository.deleteAll();

            List<OmniEdge> omniEdges = readFromJson();

            omniEdges.forEach(edge -> {
                OmniNode uEdge = edge.getU();
                OmniNode vEdge = edge.getV();

                if (uEdge.getType() == null) {
                    handleDyidRelationship(dyidRepository, cuidRepository, uEdge, vEdge);
                } else {
                    handleCuidRelationship(dyidRepository, cuidRepository, uEdge, vEdge);
                }
            });

        };
    }

    private void handleCuidRelationship(DyidRepository dyidRepository, CuidRepository cuidRepository, OmniNode uEdge, OmniNode vEdge) {
        CuidEntity cuid = getCuidEntity(cuidRepository, uEdge);
        if (null == vEdge.getType()) {
            DyidEntity toDyid = getDyidEntity(dyidRepository, vEdge);
            cuid.setCuidToDyidEdge(toDyid);
        } else {
            CuidEntity toCuid = getCuidEntity(cuidRepository, vEdge);
            cuid.setCuidToCuidEdge(toCuid);
        }

        cuidRepository.save(cuid);
    }

    private void handleDyidRelationship(DyidRepository dyidRepository, CuidRepository cuidRepository, OmniNode uEdge, OmniNode vEdge) {
        DyidEntity dyid = getDyidEntity(dyidRepository, uEdge);

        if (vEdge.getType() == null) {
            DyidEntity toDyid = getDyidEntity(dyidRepository, vEdge);
            dyid.setDyidToDyidEdge(toDyid);
        } else {
            CuidEntity toCuid = getCuidEntity(cuidRepository, vEdge);
            dyid.setDyidToCuidEdge(toCuid);
        }
        dyidRepository.save(dyid);
    }

    private CuidEntity getCuidEntity(CuidRepository cuidRepository, OmniNode vEdge) {
        CuidEntity cuidEntity = cuidRepository.findByCuidAndType(vEdge.getN(), vEdge.getType().getTypeString());
        cuidEntity = cuidEntity == null ? new CuidEntity(vEdge.getN(), vEdge.getType().getTypeString()) : cuidEntity;
        cuidRepository.save(cuidEntity);
        return cuidEntity;
    }

    private DyidEntity getDyidEntity(DyidRepository dyidRepository, OmniNode uEdge) {
        DyidEntity dyidEntity = dyidRepository.findByDyid(uEdge.getN());
        dyidEntity = dyidEntity == null ? new DyidEntity(uEdge.getN()) : dyidEntity;
        dyidRepository.save(dyidEntity);
        return dyidEntity;
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
