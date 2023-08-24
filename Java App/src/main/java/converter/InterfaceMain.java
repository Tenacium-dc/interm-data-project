package converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

public class InterfaceMain {

  // Set to store unsupported files encountered during processing
  private static Set<String> unsupportedFiles = new HashSet<>();

  /**
   * The main method that carries out the conversion process.
   * 
   * @param args Command-line arguments (not used in this context).
   */
  public static void main(String[] args) {
    String sourceFolder = "C:\\Users\\tejas\\Documents\\Interface Files Store";
    String outputFolder = "C:\\Users\\tejas\\Documents\\CSV Outputs";

    Set<String> infectedFiles = new HashSet<>(); // Set to store infected files detected by ClamAV

    File[] filesInSourceFolder = new File(sourceFolder).listFiles();

    if (filesInSourceFolder == null || filesInSourceFolder.length == 0) {
      System.err.println("No files found in the source folder.");
      return;
    }

    ClamAVService clamAVService = new ClamAVService(); // Initialise ClamAVService

    // Iterate through each file in the source folder
    for (File file : filesInSourceFolder) {
      try {
        if (clamAVService.ping()) {
          // Scan the file for malware
          VirusScanResult scanResult = clamAVService.scan(new FileInputStream(file));

          if (scanResult.getStatus() == VirusScanStatus.PASSED) {
            System.out.println("Passed malware scan: " + file.getName());

            // File passed malware scan, proceed with conversion
            String fileType = FileDetector.detectFileType(file.getAbsolutePath());

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
          } else {
            // File failed malware scan
            infectedFiles.add(file.getName() + ": " + scanResult.getResult());
          }
        } else {
          System.out.println("ClamAV did not respond to the ping request!");
          unsupportedFiles.add(file.getName() + ": ClamAV issue");
        }
      } catch (IOException e) {
        System.err.println("Error converting file: " + file.getName());
        e.printStackTrace();
      }
    }

    // Print the list of infected files
    for (String fileName : infectedFiles) {
      System.err.println("Malware detected in file: " + fileName);
    }

    // Print the list of unsupported files or ClamAV issues
    if (!unsupportedFiles.isEmpty()) {
      System.err.println("Unsupported file formats or ClamAV issues:");
      for (String fileName : unsupportedFiles) {
        System.err.println(fileName);
      }
    }
  }

  /**
   * Converts a given file to CSV format based on its file type.
   * 
   * @param fileType The type of the input file.
   * @param filePath The path to the input file.
   * @param outputFolder The folder where the CSV output will be stored.
   * @return The path to the generated CSV file, or null if conversion failed or unsupported.
   * @throws IOException If an I/O error occurs during the conversion process.
   */
  private static String convertFileToCsv(String fileType, String filePath, String outputFolder)
      throws IOException {
    try {
      // Use a switch statement to handle different file types and call the appropriate converter.
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
          unsupportedFiles.add("Unsupported file format: " + new File(filePath).getName());
          return null;
      }
    } catch (IllegalArgumentException e) {
      unsupportedFiles.add("Unsupported file format: " + new File(filePath).getName());
      return null;
    }
  }
}
