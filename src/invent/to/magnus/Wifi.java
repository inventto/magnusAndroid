package invent.to.magnus;

import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.wifi.WifiManager;

public final class Wifi {

	public static final String SERVER = "http://static.academi.as"; // 4

	private static WifiManager wifi;

	protected boolean connecting;

	private static Wifi instance;

	private Wifi(final Context context) {
		wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
	}

	public static Wifi getInstance(Context context) {
		if (instance == null && context != null)
			instance = new Wifi(context);
		return instance;
	}
	
	public static boolean testConnection() {
		if (wifi != null && !wifi.isWifiEnabled()) {
			return false;
		}
		URL url;
		try {
			url = new URL("http://23.239.16.249");
			HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
			urlc.setRequestProperty("Connection", "close");
			urlc.setConnectTimeout(2000);
			urlc.connect();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
