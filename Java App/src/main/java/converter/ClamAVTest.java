package converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ClamAVTest {
  public static void main(String[] args) {
    ClamAVService clamAVService = new ClamAVService();
    File file = new File(
        "C:\\Users\\tejas\\Documents\\Tenacium Projects\\Java App\\src\\main\\java\\converter\\testfile.txt");
    // File file = new File("abc.txt"); // Uncomment this line if needed

    VirusScanResult scanResult = null;

    try {
      if (clamAVService.ping()) {
        try (InputStream inputStream = new FileInputStream(file)) {
          scanResult = clamAVService.scan(inputStream);
        } catch (IOException e) {
          System.out.println("An error occurred while scanning the file: " + e.getMessage());
          scanResult = new VirusScanResult(VirusScanStatus.FAILED, e.getMessage());
        }
      } else {
        System.out.println("ClamAV did not respond to the ping request!");
        scanResult = new VirusScanResult(VirusScanStatus.CONNECTION_FAILED,
            "ClamAV did not respond to the ping request!");
      }
    } catch (Exception e) {
      System.out.println("An error occurred while scanning the file: " + e.getMessage());
      scanResult =
          new VirusScanResult(VirusScanStatus.ERROR, "An error occurred while scanning the file.");
    }

    System.out.println(scanResult);
  }
}
