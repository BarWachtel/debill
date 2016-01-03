package api.route;
import java.io.InputStream;
import javax.servlet.ServletContext;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import api.controller.ImageUploadController;
import api.service.SessionService;
import com.sun.jersey.core.header.ContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import generalutils.thread.ThreadLocalUtil;

@Path("/files")

public class ImageUploadRoute {
private static final String DIR_NAME = "Images";
private static final String SAVE_DIR = "uploadFiles";


    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@Context HttpServletRequest request, FormDataMultiPart form) {
        FormDataBodyPart filePart = form.getField("file");
        ContentDisposition headerOfFilePart = filePart.getContentDisposition();
        InputStream fileInputStream = filePart.getValueAs(InputStream.class);

		SessionService.AddSessionToLocalStore(request);
		SessionService.AddUserIdToSession(1);

        String output = ImageUploadController.saveFile(fileInputStream, headerOfFilePart.getFileName());
        return Response.status(200).entity(output).build();
    }
}
