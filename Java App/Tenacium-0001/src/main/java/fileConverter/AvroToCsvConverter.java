package fileConverter;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.SeekableByteArrayInput;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AvroToCsvConverter {

  public void convertAvroToCsv(InputStream avroInputStream, ByteArrayOutputStream csvOutputStream) {
    try {
      // Read Avro data and convert it to CSV
      Schema avroSchema = retrieveAvroSchema(avroInputStream);
      DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(avroSchema);
      DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(
          new SeekableByteArrayInput(csvOutputStream.toByteArray()), datumReader);

      CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(dataFileReader.getSchema().getFields()
          .stream().map(Schema.Field::name).toArray(String[]::new));
      CSVPrinter csvPrinter = new CSVPrinter(new java.io.OutputStreamWriter(System.out), csvFormat);

      for (GenericRecord record : dataFileReader) {
        List<Object> row = new ArrayList<>();
        for (Schema.Field field : avroSchema.getFields()) {
          row.add(record.get(field.name()));
        }
        csvPrinter.printRecord(row);
      }

      csvPrinter.flush();
      csvPrinter.close();
      dataFileReader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Schema retrieveAvroSchema(InputStream avroInputStream) throws IOException {
    DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
    DataFileReader<GenericRecord> dataFileReader =
        new DataFileReader<>(avroInputStream, datumReader);
    Schema avroSchema = dataFileReader.getSchema();
    dataFileReader.close();
    return avroSchema;
  }

  public static void main(String[] args) {
    // Example usage:
    AvroToCsvConverter converter = new AvroToCsvConverter();
    try {
      InputStream avroInputStream = new FileInputStream("input.avro");
      ByteArrayOutputStream csvOutputStream = new ByteArrayOutputStream();
      converter.convertAvroToCsv(avroInputStream, csvOutputStream);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
