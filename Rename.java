import java.io.File;
import java.util.Date;
import java.util.List;

public class Rename {
  private static void rename(String path) {
    List<String> filePaths = Directory.list(path);
    for (String filePath : filePaths) {
      File file = new File(filePath);
      Date date = FileUtils.getImageDate(file);
      String extension = FileUtils.getExtension(file);
      if (date != null && extension != null) {
        FileUtils.rename(file, date, extension);
      }
    }
  }

  public static void main(String[] args) {
    if (args.length > 0) {
      rename(args[0]);
    }
  }
}
