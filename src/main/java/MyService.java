import Entities.DyidRepository;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.hadoop.io.AvroKeyValue;
import org.apache.avro.specific.SpecificDatumReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;

@Service
public class MyService {

    @Autowired
    private DyidRepository repository;

    public MyService(DyidRepository repository) {
        this.repository = repository;
    }

    public MyService() {
    }


    @Transactional
    public void doWork() throws IOException {

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
            AvroKeyValue avroKeyValue = new AvroKeyValue(kv);
            System.out.println(avroKeyValue);
        }


//        GenericRecord record = null;
//        while (dataFileReader.hasNext()) {
//            record = dataFileReader.next(record);
//            System.out.println(record);
//        }
//
////        User user = null;
////        while (dataFileReader.hasNext()) {
////// Reuse user object by passing it to next(). This saves us from
////// allocating and garbage collecting many objects for files with
////// many items.
////            user = dataFileReader.next(user);
////            System.out.println(user);
////        }
////
////        Person jon = new Person("Jon");
////        Person emil = new Person("Emil");
////        Person rod = new Person("Rod");
////
////        emil.knows(jon);
////        emil.knows(rod);
////
////        // Persist entities and relationships to graph database
////        personRepository.save(emil);
////
////        for (Person friend : emil.getFriends()) {
////            System.out.println("Friend: " + friend);
////        }
////
////        // Control loading depth
////        Person thatSamejon = personRepository.findOne(id, 2);
////        for (Person friend : jon.getFriends()) {
////            System.out.println("Jon's friends to depth 2: " + friend);
////        }
    }

    public static void main(String[] args) throws IOException {
        MyService myService = new MyService();
        myService.doWork();
    }
}