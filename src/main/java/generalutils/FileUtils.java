package generalutils;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by user on 12/12/2015.
 */
public class FileUtils {

	public static List<String> readLines(String fileName) {
		List<String> fileLines = new ArrayList<>();

		try (BufferedReader reader =
					 new BufferedReader(new java.io.FileReader(fileName));
		) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				fileLines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fileLines;
	}

	public static String saveBinaryFile(File file) {
		String fileName = "bin" + file.hashCode();

		try {
			Path path = Paths.get(fileName);
			Files.write(path, IOUtils.toByteArray(new FileInputStream(file)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fileName;
	}

	public static void removeFile(String fileName) {
		try {

			File file = new File(fileName);

			if (file.delete()) {
				System.out.println(file.getName() + " is deleted!");
			} else {
				System.out.println("Delete operation is failed.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static File loadFile(String imgName) {
		return new File(imgName);
	}
}
