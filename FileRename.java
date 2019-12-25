import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileRename {
    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    public static void main(String[] args) {
        List<String> filePaths = Directory.list(args[0], "(.*)_decrypted(.*)", "(.*) - 複製(.*)");

        for (String filePath: filePaths) {
            Path path = new File(filePath).toPath();
            String fileExtension = null;
            try {
                String mimeType = Files.probeContentType(path);
                if (mimeType.equals("image/jpeg")) {
                    fileExtension = ".jpg";
                } else if (mimeType.equals("image/png")) {
                    fileExtension = ".png";
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {

            }
            if (fileExtension == null) {
                File source = new File(filePath);
                File destination = new File(filePath + "." + "jpg");
                try {
                    copyFileUsingStream(source, destination);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
