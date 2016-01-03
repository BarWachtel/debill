package api.service;

import generalutils.thread.ThreadLocalUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by user on 03/01/2016.
 */
public class SessionService {
	private static final boolean CREATE_SESSION_IF_NONE_EXISTS = true;

	public static void addSessionToLocalStore(HttpServletRequest request) {
		ThreadLocalUtil.set(ThreadLocalUtil.USER_SESSION, request.getSession(CREATE_SESSION_IF_NONE_EXISTS));
	}

	public static void setUserId(int userId) {
		HttpSession httpSession = ThreadLocalUtil.getUserSession();
		httpSession.setAttribute(ThreadLocalUtil.USER_ID, userId);
	}

	public static int getUserId() {
		HttpSession httpSession = ThreadLocalUtil.getUserSession();
		return (int) httpSession.getAttribute(ThreadLocalUtil.USER_ID);
	}
}
