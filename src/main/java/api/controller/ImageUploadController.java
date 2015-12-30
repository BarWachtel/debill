package api.controller;

import com.sun.jersey.core.header.ContentDisposition;
import core.Core;
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
public class ImageUploadController {
    private static final String SERVER_UPLOAD_LOCATION_FOLDER = "/path/to/uploads/";


    public static String saveFile(InputStream fileInputStream,ContentDisposition headerOfFilePart) {
        String status =  "file saved to ";

        byte[] bytes = new byte[0];
        try {
            bytes = IOUtils.toByteArray(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileName = SERVER_UPLOAD_LOCATION_FOLDER + headerOfFilePart.getFileName();
        File sFile = null;
        try {
            sFile = writeFile(bytes,fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String output;
        output = "saved to " + fileName;

        System.out.println("fileName :" +output);
        //System.out.println("savedPath :" +savedPath);


//        //**for check
//        String s = "C://Users/Dima/Desktop/Upload_Files/";
//        InputStream targetStream = null;
//        System.out.println("fileName :" +fileName);
//        try {
//            targetStream = FileUtils.openInputStream(sFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            writeFile(IOUtils.toByteArray(targetStream),s + headerOfFilePart.getFileName());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //for check

        status = status+ sFile.getAbsolutePath();
        Core c;
        c = new Core();
        c.createNewBill(sFile);

        return status;
    }

    private static File writeFile(byte[] content, String filename) throws IOException {
        File file;
        FileUtils.writeByteArrayToFile(new File(filename), content);
        file = generalutils.FileUtils.loadFile(filename);
        return file;
    }
}
