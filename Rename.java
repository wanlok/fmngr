import com.drew.imaging.FileType;
import com.drew.imaging.FileTypeDetector;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Rename {
  private static void rename(String path) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC +8"));
    List<String> filePaths = Directory.list(path);
    for (String filePath : filePaths) {
      File file = new File(filePath);
      try {
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        com.drew.metadata.Directory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
        InputStream inputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        FileType fileType = FileTypeDetector.detectFileType(bufferedInputStream);
        String extension = null;
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
        if (extension != null) {
          String newFileName = simpleDateFormat.format(date) + "." + extension;
          System.out.println(file.getName() + " -> " + newFileName);
          Path source = Paths.get(file.getAbsolutePath());
          Files.move(source, source.resolveSibling(newFileName));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) {
    if (args.length > 0) {
      rename(args[0]);
    }
  }
}
