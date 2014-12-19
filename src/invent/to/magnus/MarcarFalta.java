package invent.to.magnus;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class MarcarFalta extends AsyncTask<Void, Void, Void>{

	@Override
	protected Void doInBackground(Void... params) {
		HttpPost httppost = new HttpPost(GlobalActivity.ADDRESS + "/registro_presenca/marcar_falta");
		try {
			Log.i("Error", GlobalActivity.ADDRESS + "/registro_presenca/marcar_falta");
			DefaultHttpClient client = new DefaultHttpClient();
			client.execute(httppost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
