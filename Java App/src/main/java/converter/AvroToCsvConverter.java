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

  // Private constructor to prevent instantiation, as this is a utility class.
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
    // Open the Avro file for reading using try-with-resources to ensure proper resource management.
    try (DataFileReader<GenericRecord> dataFileReader = openAvroFile(avroFilePath)) {
      // Get the schema from the Avro file.
      Schema schema = dataFileReader.getSchema();
      StringBuilder csvContent = new StringBuilder();

      // Construct the CSV header using the Avro schema's field names.
      for (Schema.Field field : schema.getFields()) {
        csvContent.append(field.name()).append(",");
      }
      csvContent.setLength(csvContent.length() - 1); // Remove trailing comma
      csvContent.append("\n");

      // Append records to the CSV content.
      for (GenericRecord avroRecord : dataFileReader) {
        // Extract data from each field of the Avro record and append to CSV content.
        for (Schema.Field field : schema.getFields()) {
          csvContent.append(avroRecord.get(field.name())).append(",");
        }
        csvContent.setLength(csvContent.length() - 1); // Remove trailing comma
        csvContent.append("\n");
      }

      // Extract the source Avro file name without extension.
      String avroFileName = new File(avroFilePath).getName();
      String avroFileNameWithoutExtension =
          avroFileName.substring(0, avroFileName.lastIndexOf('.'));

      // Create a CSV file with the same name as the source Avro file.
      String csvFileName = avroFileNameWithoutExtension + ".csv";
      String csvFilePath = outputFolder + File.separator + csvFileName;
      // Write the CSV content to the created CSV file using try-with-resources.
      try (BufferedWriter bw =
          new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFilePath)))) {
        bw.write(csvContent.toString());
      }

      // Return the path of the generated CSV file.
      return csvFilePath;
    }
  }

  /**
   * Opens an Avro file for reading and returns a DataFileReader instance.
   *
   * @param avroFilePath The path of the input Avro file.
   * @return A DataFileReader instance for reading Avro records.
   * @throws IOException If an I/O error occurs.
   */
  private static DataFileReader<GenericRecord> openAvroFile(String avroFilePath)
      throws IOException {
    // Create a File instance from the provided Avro file path.
    File file = new File(avroFilePath);
    // Create a GenericDatumReader to read Avro records.
    DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
    // Return a DataFileReader instance for the Avro file using the datumReader.
    return new DataFileReader<>(file, datumReader);
  }
}
