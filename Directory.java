import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Directory {
    public static List<String> list(String directoryPath, String pattern, String rejectPattern) {
        List<String> filePaths = new ArrayList<String>();
        File[] files = new File(directoryPath).listFiles();
        for (File file: files) {
            if (file.isDirectory()) {
                for (String filePath: list(file.getAbsolutePath(), pattern, rejectPattern)) {
                    filePaths.add(filePath);
                }
            } else {
                String filePath = file.getAbsolutePath();
                if (StringUtils.isProvided(pattern) && StringUtils.isProvided(rejectPattern)) {
                    if (filePath.matches(pattern) && !filePath.matches(rejectPattern)) {
                        filePaths.add(filePath);
                    }
                } else if (StringUtils.isProvided(pattern) && !StringUtils.isProvided(rejectPattern)) {
                    if (filePath.matches(pattern)) {
                        filePaths.add(filePath);
                    }
                } else if (!StringUtils.isProvided(pattern) && StringUtils.isProvided(rejectPattern)) {
                    if (!filePath.matches(rejectPattern)) {
                        filePaths.add(filePath);
                    }
                } else {
                    filePaths.add(filePath);
                }
            }
        }
        return filePaths;
    }

    public static List<String> list(String directoryPath) {
        System.out.println(directoryPath);
        return list(directoryPath, null, null);
    }
}
