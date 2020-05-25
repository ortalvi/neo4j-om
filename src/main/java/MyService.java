import Entities.DyidEntity;
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
            AvroKeyValue<Long, Long> avroKeyValue = new AvroKeyValue(kv);
            DyidEntity dyid = new DyidEntity(avroKeyValue.getKey());
            DyidEntity leader = new DyidEntity(avroKeyValue.getValue());
            dyid.leader(leader);
            repository.save(dyid);
        }
    }

    public static void main(String[] args) throws IOException {
        MyService myService = new MyService();
        myService.doWork();
    }
}