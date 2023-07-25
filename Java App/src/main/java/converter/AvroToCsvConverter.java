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

/**
 * A utility class to convert Avro files to CSV format.
 */
public class AvroToCsvConverter {

  private AvroToCsvConverter() {}

  /**
   * Converts an Avro file to CSV format.
   *
   * @param avroFilePath The path of the input Avro file.
   * @param outputFolder The output folder to save the generated CSV file.
   * @return The path of the generated CSV file, or null if conversion fails.
   * @throws IOException If an I/O error occurs.
   */
  public static String convertAvroToCsv(String avroFilePath, String outputFolder)
      throws IOException {
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
      for (GenericRecord avroRecord : dataFileReader) {
        for (Schema.Field field : schema.getFields()) {
          csvContent.append(avroRecord.get(field.name())).append(",");
        }
        csvContent.setLength(csvContent.length() - 1); // Remove trailing comma
        csvContent.append("\n");
      }

      // Extract the source Avro file name without extension
      String avroFileName = new File(avroFilePath).getName();
      String avroFileNameWithoutExtension =
          avroFileName.substring(0, avroFileName.lastIndexOf('.'));

      // Write CSV content to file with the same name as the source Avro file
      String csvFileName = avroFileNameWithoutExtension + ".csv";
      String csvFilePath = outputFolder + File.separator + csvFileName;
      try (BufferedWriter bw =
          new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFilePath)))) {
        bw.write(csvContent.toString());
      }

      // Return the path of the generated CSV file
      return csvFilePath;
    }
  }

  /**
   * Opens an Avro file for reading.
   *
   * @param avroFilePath The path of the input Avro file.
   * @return A DataFileReader instance for reading Avro records.
   * @throws IOException If an I/O error occurs.
   */
  private static DataFileReader<GenericRecord> openAvroFile(String avroFilePath)
      throws IOException {
    File file = new File(avroFilePath);
    DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
    return new DataFileReader<>(file, datumReader);
  }
}
