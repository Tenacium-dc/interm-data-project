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
 * A utility class to convert Parquet files to CSV format.
 */
public class ParquetToCsvConverter {

  // Private constructor to prevent instantiation, as this is a utility class.
  private ParquetToCsvConverter() {}

  /**
   * Converts a Parquet file to a CSV file.
   *
   * @param inputParquetFile The path to the input Parquet file.
   * @param outputFolder The output folder to save the generated CSV file.
   * @return The path of the generated CSV file, or null if conversion fails.
   * @throws IOException If an I/O error occurs.
   */
  public static String convertParquetToCsv(String inputParquetFile, String outputFolder)
      throws IOException {
    // Create a File instance for the input Parquet file.
    File parquetFile = new File(inputParquetFile);

    // Check if the Parquet file exists.
    if (!parquetFile.exists()) {
      System.err.println("Parquet file not found: " + inputParquetFile);
      return null;
    }

    // Check if the input path is a file.
    if (!parquetFile.isFile()) {
      System.err.println("Input path is not a file: " + inputParquetFile);
      return null;
    }

    // Open the Parquet file for reading using try-with-resources to ensure proper resource
    // management.
    try (DataFileReader<GenericRecord> dataFileReader = openParquetFile(inputParquetFile)) {
      // Get the schema of the Parquet file.
      Schema schema = dataFileReader.getSchema();
      // StringBuilder to store the CSV content.
      StringBuilder csvContent = new StringBuilder();

      // Construct CSV header.
      for (Schema.Field field : schema.getFields()) {
        csvContent.append(field.name()).append(",");
      }
      csvContent.setLength(csvContent.length() - 1); // Remove trailing comma.
      csvContent.append("\n");

      // Append records to CSV.
      for (GenericRecord record : dataFileReader) {
        for (Schema.Field field : schema.getFields()) {
          csvContent.append(record.get(field.name())).append(",");
        }
        csvContent.setLength(csvContent.length() - 1); // Remove trailing comma.
        csvContent.append("\n");
      }

      // Extract the source Parquet file name without extension.
      String parquetFileName = parquetFile.getName();
      String parquetFileNameWithoutExtension =
          parquetFileName.substring(0, parquetFileName.lastIndexOf('.'));

      // Write CSV content to file with the same name as the source Parquet file.
      String csvFileName = parquetFileNameWithoutExtension + ".csv";
      String csvFilePath = outputFolder + File.separator + csvFileName;
      try (BufferedWriter bw =
          new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFilePath)))) {
        bw.write(csvContent.toString());
      }

      // Return the path of the generated CSV file.
      return csvFilePath;
    } catch (IOException e) {
      System.err.println("Error converting Parquet file: " + parquetFile.getName());
      e.printStackTrace();
      return null; // Return null in case of any error during conversion.
    }
  }

  /**
   * Opens the Parquet file for reading.
   *
   * @param inputParquetFile The path to the input Parquet file.
   * @return The DataFileReader instance for the Parquet file.
   * @throws IOException If an I/O error occurs.
   */
  private static DataFileReader<GenericRecord> openParquetFile(String inputParquetFile)
      throws IOException {
    // Create a File instance for the input Parquet file.
    File file = new File(inputParquetFile);
    // Create a DatumReader to read Avro GenericRecords.
    DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
    // Return the DataFileReader for the Parquet file.
    return new DataFileReader<>(file, datumReader);
  }
}
