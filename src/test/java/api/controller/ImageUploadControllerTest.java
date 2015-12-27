package api.controller;

import generalutils.FileUtils;
import junit.framework.TestCase;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by Dima on 27/12/2015.
 */
public class ImageUploadControllerTest extends TestCase {

    public void testSaveFile() throws Exception {
        String imgName = "img/text.png";
        File imgFile = FileUtils.loadFile(imgName);

        imgFile.setReadable(true);
        imgFile.setWritable(true);
        imgFile.setExecutable(true);
        System.setProperty("upload.location", "/");

        InputStream in = new FileInputStream(imgFile);
        ImageUploadController.saveFile("text.png",in);
    }
}