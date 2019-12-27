import com.drew.imaging.FileType;
import com.drew.imaging.FileTypeDetector;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class FileUtils {
  private static void rename(File file, String name, String extension, int attempt) {
    try {
      Path source = Paths.get(file.getAbsolutePath());
      String newName;
      if (attempt < 10) {
        newName = name + "0" + attempt;
      } else {
        newName = name + attempt;
      }
      newName += "." + extension;
      System.out.println(file.getName() + " -> " + newName);
      Files.move(source, source.resolveSibling(newName));
    } catch (Exception e) {
      rename(file, name, extension, attempt + 1);
    }
  }

  public static void rename(File file, Date date, String extension) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC +8"));
    FileUtils.rename(file, simpleDateFormat.format(date), extension, 1);
  }

  public static Date getImageDate(File file) {
    Date date = null;
    try {
      Metadata metadata = ImageMetadataReader.readMetadata(file);
      Directory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
      date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return date;
  }

  public static String getExtension(File file) {
    String extension = null;
    try {
      InputStream inputStream = new FileInputStream(file);
      BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
      FileType fileType = FileTypeDetector.detectFileType(bufferedInputStream);
      if (fileType == FileType.Jpeg) {
        extension = "jpg";
      } else if (fileType == FileType.Png) {
        extension = "png";
      } else if (fileType == FileType.Gif) {
        extension = "gif";
      } else if (fileType == FileType.Bmp) {
        extension = "bmp";
      }
      bufferedInputStream.close();
      inputStream.close();
    } catch (Exception e) {
      e.printStackTrace(); 
    }
    return extension;
  }
}