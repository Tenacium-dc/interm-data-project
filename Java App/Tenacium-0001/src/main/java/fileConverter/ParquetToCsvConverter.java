package fileConverter;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.avro.file.SeekableByteArrayInput;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.util.Utf8;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;

public class ParquetToCsvConverter {

  public void convertParquetToCsv(InputStream parquetInputStream,
      ByteArrayOutputStream csvOutputStream) {
    try {
      // Read Parquet data and convert it to CSV
      DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
      Builder<GenericRecord> parquetReaderBuilder =
          AvroParquetReader.builder(new SeekableByteArrayInput(parquetInputStream));
      ParquetReader<GenericRecord> parquetReader = parquetReaderBuilder.build();

      CSVFormat csvFormat = CSVFormat.DEFAULT;
      CSVPrinter csvPrinter =
          new CSVPrinter(new java.io.OutputStreamWriter(csvOutputStream), csvFormat);

      while (true) {
        GenericRecord record = parquetReader.read();
        if (record == null) {
          break;
        }

        List<Object> row = new ArrayList<>();
        for (org.apache.avro.Schema.Field field : record.getSchema().getFields()) {
          Object value = record.get(field.name());
          // Convert Utf8 values to String
          if (value instanceof Utf8) {
            value = value.toString();
          }
          row.add(value);
        }
        csvPrinter.printRecord(row);
      }

      csvPrinter.flush();
      csvPrinter.close();
      parquetReader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    // Example usage:
    ParquetToCsvConverter converter = new ParquetToCsvConverter();
    try {
      InputStream parquetInputStream = new FileInputStream("input.parquet");
      ByteArrayOutputStream csvOutputStream = new ByteArrayOutputStream();
      converter.convertParquetToCsv(parquetInputStream, csvOutputStream);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
