package converter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;

public class AvroToCsvConverter {
  private static int fileCounter = 1;

  public static void convertAvroToCsv(String avroFilePath, String outputFolder) throws IOException {
    try (DataFileReader<GenericRecord> dataFileReader = openAvroFile(avroFilePath)) {
      Schema schema = dataFileReader.getSchema();
      StringBuilder csvContent = new StringBuilder();

      // Construct CSV header
      for (Schema.Field field : schema.getFields()) {
        csvContent.append(field.name()).append(",");
      }
      csvContent.setLength(csvContent.length() - 1); // Remove trailing comma
      csvContent.append("\n");

      // Append records to CSV
      for (GenericRecord record : dataFileReader) {
        for (Schema.Field field : schema.getFields()) {
          csvContent.append(record.get(field.name())).append(",");
        }
        csvContent.setLength(csvContent.length() - 1); // Remove trailing comma
        csvContent.append("\n");
      }

      // Write CSV content to file
      String csvFileName = "output_" + fileCounter + ".csv";
      String csvFilePath = outputFolder + File.separator + csvFileName;
      try (BufferedWriter bw =
          new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFilePath)))) {
        bw.write(csvContent.toString());
      }

      fileCounter++;
    }
  }

  private static DataFileReader<GenericRecord> openAvroFile(String avroFilePath)
      throws IOException {
    File file = new File(avroFilePath);
    DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
    return new DataFileReader<>(file, datumReader);
  }
}

