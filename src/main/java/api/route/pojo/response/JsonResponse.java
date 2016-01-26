package api.route.pojo.response;

import com.sun.istack.Nullable;
import database.entity.Entity;
import database.entity.User;

import java.util.Objects;

/**
 * Created by user on 10/01/2016.
 */
public class JsonResponse {
	boolean error;
	Object content;
	String errorMsg;

	private JsonResponse() {
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public static JsonResponse ok(Object object) {
		JsonResponse jsonResponse = new JsonResponse();
		jsonResponse.setContent(object);
		return jsonResponse;
	}

	public static JsonResponse okIfLegalId(Entity entity) {
		if (entity.hasLegalId()) {
			return ok(entity);
		} else {
			return error("Failed to create");
		}
	}

	public static JsonResponse error(@Nullable String errorMsg) {
		JsonResponse jsonResponse = new JsonResponse();
		jsonResponse.setError(true);

		if (errorMsg == null) {
			errorMsg = "No error message supplied";
		}

		jsonResponse.setErrorMsg(errorMsg);
		return jsonResponse;
	}
}
