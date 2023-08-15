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

  public static final String API_KEY = "API KEY";

  /**
   * Main entry point of the program.
   *
   * @param args Command-line arguments (not used in this implementation).
   */
  public static void main(String[] args) {
    // The file directories are for local testing; they will be changed in the AWS instance.
    String sourceFolder = "C:\\Users\\tejas\\Documents\\Interface Files Store";
    String outputFolder = "C:\\Users\\tejas\\Documents\\CSV Outputs";

    // A set to store names of unsupported files during conversion.
    Set<String> unsupportedFiles = new HashSet<>();

    // Get a list of files from the source folder.
    File[] filesInSourceFolder = new File(sourceFolder).listFiles();

    if (filesInSourceFolder == null || filesInSourceFolder.length == 0) {
      System.err.println("No files found in the source folder.");
      return; // Exit the program if no files are found.
    }

    // Loop through each file in the source folder for conversion.
    for (File file : filesInSourceFolder) {
      try {
        // Detect the file type using the FileDetector class.
        String fileType = FileDetector.detectFileType(file.getAbsolutePath());
        // If the file type is not supported, add it to the set and continue to the next file.
        if (fileType.equals("File type is not supported")) {
          unsupportedFiles.add(file.getName());
          continue;
        }
        // Convert the file to CSV and get the path of the generated CSV file.
        String csvFilePath = convertFileToCsv(fileType, file.getAbsolutePath(), outputFolder);
        if (csvFilePath != null) {
          // Print success message if the conversion was successful.
          System.out.println(
              "Converted " + fileType + " file: " + file.getName() + " to CSV: " + csvFilePath);
        } else {
          // Print error message if the conversion failed.
          System.out.println("Failed to convert " + fileType + " file: " + file.getName());
        }
      } catch (IOException e) {
        // Print error message and stack trace if an I/O error occurs during conversion.
        System.err.println("Error converting file: " + file.getName());
        e.printStackTrace();
      }
    }

    // Print unsupported files that were detected during conversion.
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
      // Use a switch statement to handle different file types and call the appropriate converter.
      switch (fileType) {
        case "Excel":
          return ExcelToCsvConverter.convertExcelToCsv(filePath, outputFolder);
        case "JSON":
          // Read JSON data from the input file and convert it to CSV using JsonToCsvConverter.
          String jsonData = new String(Files.readAllBytes(new File(filePath).toPath()));
          return JsonToCsvConverter.convertJsonStringToCsv(jsonData, outputFolder);
        case "Avro":
          return AvroToCsvConverter.convertAvroToCsv(filePath, outputFolder);
        case "Parquet":
          return ParquetToCsvConverter.convertParquetToCsv(filePath, outputFolder);
        default:
          // Throw an exception for unsupported file types.
          throw new IOException("Unsupported file format: " + fileType);
      }
    } catch (IllegalArgumentException e) {
      // Catch any exceptions related to unsupported file formats and print an error message.
      System.err.println("Unsupported file format: " + new File(filePath).getName());
      return null;
    }
  }
}
