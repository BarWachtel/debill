package api.route;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import api.controller.ImageUploadController;
import com.sun.jersey.core.header.ContentDisposition;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;


@Path("/files")
public class ImageUploadRoute {
private static final String DIR_NAME = "Images";
    @POST

    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)

    public Response uploadFile(FormDataMultiPart form) {
        FormDataBodyPart filePart = form.getField("file");
        ContentDisposition headerOfFilePart = filePart.getContentDisposition();
        InputStream fileInputStream = filePart.getValueAs(InputStream.class);

        String output;
        output = ImageUploadController.saveFile(headerOfFilePart.getFileName(), fileInputStream);
        return Response.status(200).entity(output).build();

    }
}