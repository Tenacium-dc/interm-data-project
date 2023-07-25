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
    File parquetFile = new File(inputParquetFile);

    if (!parquetFile.exists()) {
      System.err.println("Parquet file not found: " + inputParquetFile);
      return null;
    }

    if (!parquetFile.isFile()) {
      System.err.println("Input path is not a file: " + inputParquetFile);
      return null;
    }

    try (DataFileReader<GenericRecord> dataFileReader = openParquetFile(inputParquetFile)) {
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

      // Extract the source Parquet file name without extension
      String parquetFileName = parquetFile.getName();
      String parquetFileNameWithoutExtension =
          parquetFileName.substring(0, parquetFileName.lastIndexOf('.'));

      // Write CSV content to file with the same name as the source Parquet file
      String csvFileName = parquetFileNameWithoutExtension + ".csv";
      String csvFilePath = outputFolder + File.separator + csvFileName;
      try (BufferedWriter bw =
          new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFilePath)))) {
        bw.write(csvContent.toString());
      }

      // Return the path of the generated CSV file
      return csvFilePath;
    } catch (IOException e) {
      System.err.println("Error converting Parquet file: " + parquetFile.getName());
      e.printStackTrace();
      return null; // Return null in case of any error during conversion
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
    File file = new File(inputParquetFile);
    DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
    return new DataFileReader<>(file, datumReader);
  }
}
