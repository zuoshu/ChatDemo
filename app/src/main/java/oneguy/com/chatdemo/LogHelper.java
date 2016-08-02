package oneguy.com.chatdemo;

import android.util.Log;


public class LogHelper {

	private static boolean DEBUG = BuildConfig.DEBUG;

	public static void warn(Class c, String msg) {
		if (DEBUG) {
			Log.w("warn：" + c, msg);
		}
	}

	public static void warn(Class c, int msg) {
		if (DEBUG) {
			Log.w("warn：" + c, String.valueOf(msg));
		}
	}

	public static void warn(Class c, long msg) {
		if (DEBUG) {
			Log.w("warn：" + c, String.valueOf(msg));
		}
	}

	public static void warn(Class c, float msg) {
		if (DEBUG) {
			Log.w("warn：" + c, String.valueOf(msg));
		}
	}

	public static void warn(Class c, double msg) {
		if (DEBUG) {
			Log.w("warn：" + c, String.valueOf(msg));
		}
	}

	public static void warn(Class c, boolean msg) {
		if (DEBUG) {
			Log.w("warn：" + c, String.valueOf(msg));
		}
	}

	public static void warn(String flag, String msg) {
		if (DEBUG) {
			Log.w("warn：" + flag, msg);
		}
	}

	public static void warn(String flag, int msg) {
		if (DEBUG) {
			Log.w("warn：" + flag, String.valueOf(msg));
		}
	}

	public static void warn(String flag, long msg) {
		if (DEBUG) {
			Log.w("warn：" + flag, String.valueOf(msg));
		}
	}

	public static void warn(String flag, float msg) {
		if (DEBUG) {
			Log.w("warn：" + flag, String.valueOf(msg));
		}
	}

	public static void warn(String flag, double msg) {
		if (DEBUG) {
			Log.w("warn：" + flag, String.valueOf(msg));
		}
	}

	public static void warn(String flag, boolean msg) {
		if (DEBUG) {
			Log.w("warn：" + flag, String.valueOf(msg));
		}
	}

	public static void e(String flag, Object o) {
		if (DEBUG) {
			Log.e("error：" + flag, String.valueOf(o));
		}
	}

	public static void info(Class c, String msg) {
		if (DEBUG) {
			Log.i("info：" + c, msg);
		}
	}

	public static void info(Class c, int msg) {
		if (DEBUG) {
			Log.i("info：" + c, String.valueOf(msg));
		}
	}

	public static void info(Class c, long msg) {
		if (DEBUG) {
			Log.i("info：" + c, String.valueOf(msg));
		}
	}

	public static void info(Class c, float msg) {
		if (DEBUG) {
			Log.i("info：" + c, String.valueOf(msg));
		}
	}

	public static void info(Class c, double msg) {
		if (DEBUG) {
			Log.i("info：" + c, String.valueOf(msg));
		}
	}

	public static void info(Class c, boolean msg) {
		if (DEBUG) {
			Log.i("info：" + c, String.valueOf(msg));
		}
	}

	public static void info(String flag, String msg) {
		if (DEBUG) {
			Log.i("info：" + flag, msg);
		}
	}

	public static void info(String flag, int msg) {
		if (DEBUG) {
			Log.i("info：" + flag, String.valueOf(msg));
		}
	}

	public static void info(String flag, long msg) {
		if (DEBUG) {
			Log.i("info：" + flag, String.valueOf(msg));
		}
	}

	public static void info(String flag, float msg) {
		if (DEBUG) {
			Log.i("info：" + flag, String.valueOf(msg));
		}
	}

	public static void info(String flag, double msg) {
		if (DEBUG) {
			Log.i("info：" + flag, String.valueOf(msg));
		}
	}

	public static void info(String flag, boolean msg) {
		if (DEBUG) {
			Log.i("info：" + flag, String.valueOf(msg));
		}
	}
}
