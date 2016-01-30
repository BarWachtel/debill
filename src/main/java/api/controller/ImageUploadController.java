package api.controller;

import com.sun.jersey.core.header.ContentDisposition;
import core.Core;
import database.entity.Bill;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletContext;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

public class ImageUploadController {
	private static final String SERVER_UPLOAD_LOCATION_FOLDER = "/uploads/";
	static Logger log = Logger.getLogger("ImageUploadController");


    public static Bill createBillFromImage(InputStream fileInputStream, String fileName) {
        String status =  "file saved to ";

        byte[] bytes = new byte[0];
        try {
            bytes = IOUtils.toByteArray(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String _fileName = SERVER_UPLOAD_LOCATION_FOLDER + fileName;
        File sFile = null;
        try {
            sFile = writeFile(bytes,_fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String output;
        output = "saved to " + _fileName;

        System.out.println("fileName :" +output);

        log.info(status + sFile.getAbsolutePath());

        //sending image to parser
        Core core = new Core();
        return core.createNewBill(sFile);
    }

    private static File writeFile(byte[] content, String filename) throws IOException {
        File file;
        FileUtils.writeByteArrayToFile(new File(filename), content);
        file = generalutils.FileUtils.loadFile(filename);
        return file;
    }
}
