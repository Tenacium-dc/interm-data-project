package converter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

/**
 * The main class to convert files of different types to CSV format.
 */
public class InterfaceMain {

  /**
   * Main entry point of the program.
   *
   * @param args Command-line arguments (not used in this implementation).
   */
  public static void main(String[] args) {
    // The file directories are for local testing; they will be changed in the AWS instance.
    String sourceFolder = "C:\\Users\\tejas\\Documents\\Interface Files Store";
    String outputFolder = "C:\\Users\\tejas\\Documents\\CSV Outputs";

    Set<String> unsupportedFiles = new HashSet<>();

    File[] filesInSourceFolder = new File(sourceFolder).listFiles();
    if (filesInSourceFolder != null) {
      for (File file : filesInSourceFolder) {
        try {
          String fileType = FileDetector.detectFileType(file.getAbsolutePath());
          if (fileType.equals("File type is not supported")) {
            unsupportedFiles.add(file.getName());
            continue;
          }
          String csvFilePath = convertFileToCsv(fileType, file.getAbsolutePath(), outputFolder);
          if (csvFilePath != null) {
            System.out.println(
                "Converted " + fileType + " file: " + file.getName() + " to CSV: " + csvFilePath);
          } else {
            System.out.println("Failed to convert " + fileType + " file: " + file.getName());
          }
        } catch (IOException e) {
          System.err.println("Error converting file: " + file.getName());
          e.printStackTrace();
        }
      }
    }

    // Print unsupported files
    for (String fileName : unsupportedFiles) {
      System.err.println("File type is not supported: " + fileName);
    }
  }

  /**
   * Converts a file of a given type to CSV format.
   *
   * @param fileType The type of the file.
   * @param filePath The path of the input file.
   * @param outputFolder The output folder to save the generated CSV file.
   * @return The path of the generated CSV file, or null if conversion fails.
   * @throws IOException If an I/O error occurs.
   */
  private static String convertFileToCsv(String fileType, String filePath, String outputFolder)
      throws IOException {
    try {
      switch (fileType) {
        case "Excel":
          return ExcelToCsvConverter.convertExcelToCsv(filePath, outputFolder);
        case "JSON":
          String jsonData = new String(Files.readAllBytes(new File(filePath).toPath()));
          return JsonToCsvConverter.convertJsonStringToCsv(jsonData, outputFolder);
        case "Avro":
          return AvroToCsvConverter.convertAvroToCsv(filePath, outputFolder);
        case "Parquet":
          return ParquetToCsvConverter.convertParquetToCsv(filePath, outputFolder);
        default:
          throw new IOException("Unsupported file format: " + fileType);
      }
    } catch (IllegalArgumentException e) {
      System.err.println("Unsupported file format: " + new File(filePath).getName());
      return null;
    }
  }
}
