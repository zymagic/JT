package t;

public class Utils {

	private static final boolean EN = false;

	public static final void log(String msg) {
		if (EN) {
			System.out.println("show log");
		}

		System.out.println(msg);
	}

}
