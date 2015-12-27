package api.controller;

import com.sun.jersey.core.header.ContentDisposition;
import core.Core;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Created by Dima on 27/12/2015.
 */
public class ImageUploadController {


    public static String saveFile(String i_fileName, InputStream i_fileInputStream) {
        Core core = new Core();

        File imageFile = saveFile(i_fileInputStream);
        core.handleNewBill(imageFile);
        String output = "File saved to server location using FormDataMultiPart : " ;
        return output;
    }

    private static File saveFile(InputStream uploadedInputStream) {

        File uploads = new File(System.getProperty("upload.location"));
        File file = new File(uploads, "somefilename.ext");

        try {
            Files.copy(uploadedInputStream, file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}
