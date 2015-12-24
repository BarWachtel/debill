package generalutils.thread;

/**
 * Created by user on 24/12/2015.
 */
public class ThreadLocalUtil {
	public static String USER_SESSION = "userSession";

	private final static java.lang.ThreadLocal<ThreadVariables> THREAD_VARIABLES = new java.lang.ThreadLocal<ThreadVariables>() {

		/**
		 * @see java.lang.ThreadLocal#initialValue()
		 */
		@Override
		protected ThreadVariables initialValue() {
			return new ThreadVariables();
		}
	};

	public static Object get(String name) {
		return THREAD_VARIABLES.get().get(name);
	}

	public static Object get(String name, InitialValue initialValue) {
		Object o = THREAD_VARIABLES.get().get(name);
		if (o == null) {
			THREAD_VARIABLES.get().put(name, initialValue.create());
			return get(name);
		} else {
			return o;
		}
	}

	public static void set(String name, Object value) {
		THREAD_VARIABLES.get().put(name, value);
	}

	public static void destroy() {
		THREAD_VARIABLES.remove();
	}
}


