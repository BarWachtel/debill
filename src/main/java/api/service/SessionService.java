package api.service;

import generalutils.thread.ThreadLocalUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionService {
	private static final boolean CREATE_SESSION_IF_NONE_EXISTS = true;

	public static void addSessionToLocalStore(HttpServletRequest request) {
		ThreadLocalUtil.set(ThreadLocalUtil.USER_SESSION, request.getSession(CREATE_SESSION_IF_NONE_EXISTS));
	}

	public static void setUserId(int userId) {
		HttpSession httpSession = ThreadLocalUtil.getUserSession();
		System.out.println("Setting id " + userId + " in session: " + httpSession.getId());
		httpSession.setAttribute(ThreadLocalUtil.USER_ID, userId);
	}

	public static int getUserIdFromStoredSession() {
		HttpSession httpSession = ThreadLocalUtil.getUserSession();
		System.out.println("Trying to get ID from session: " + httpSession.getId());
		int userId = (int) httpSession.getAttribute(ThreadLocalUtil.USER_ID);
		System.out.println("Got id " + userId + " in session: " + httpSession.getId());
		return userId;
	}

	public static int getUserIdFromRequest(HttpServletRequest request) {
		int userId = -1;
		HttpSession httpSession = request.getSession(!CREATE_SESSION_IF_NONE_EXISTS);
		if (httpSession != null) {
			userId = (int) httpSession.getAttribute(ThreadLocalUtil.USER_ID);
	}

		return userId;
	}
}
