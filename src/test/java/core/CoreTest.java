package core;

import generalutils.FileUtils;
import generalutils.thread.ThreadLocalUtil;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import redis.RedisClient;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class CoreTest extends TestCase {

	@Ignore
	@Test
	public void testCreateNewBill() throws Exception {
		Core core = new Core();
		File imgFile = FileUtils.loadFile("img/bill_crop.png");
		HttpSession httpSession = new MyHttpSession();
		httpSession.setAttribute(ThreadLocalUtil.USER_ID, 0);
		ThreadLocalUtil.set(ThreadLocalUtil.USER_SESSION, httpSession);

		int billId = core.createNewBill(imgFile);
		Assert.assertNotNull(RedisClient.getBillById(billId));

	}

	public class MyHttpSession implements HttpSession {
		private Map<String, Object> myStuff = new HashMap<>();

		@Override public long getCreationTime() {
			return 0;
		}

		@Override public String getId() {
			return null;
		}

		@Override public long getLastAccessedTime() {
			return 0;
		}

		@Override public ServletContext getServletContext() {
			return null;
		}

		@Override public void setMaxInactiveInterval(int i) {

		}

		@Override public int getMaxInactiveInterval() {
			return 0;
		}

		@Override public HttpSessionContext getSessionContext() {
			return null;
		}

		@Override public Object getAttribute(String s) {
			return myStuff.get(s);
		}

		@Override public Object getValue(String s) {
			return null;
		}

		@Override public Enumeration<String> getAttributeNames() {
			return null;
		}

		@Override public String[] getValueNames() {
			return new String[0];
		}

		@Override public void setAttribute(String s, Object o) {
			myStuff.put(s, o);
		}

		@Override public void putValue(String s, Object o) {

		}

		@Override public void removeAttribute(String s) {
			myStuff.remove(s);
		}

		@Override public void removeValue(String s) {

		}

		@Override public void invalidate() {

		}

		@Override public boolean isNew() {
			return false;
		}
	}
}