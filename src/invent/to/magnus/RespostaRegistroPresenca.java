package invent.to.magnus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class RespostaRegistroPresenca extends AsyncTask<String, String, Boolean> {

	private Context context;

	public RespostaRegistroPresenca(Context context) {
		this.context = context;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		String codigo = params[0];
		String url = GlobalActivity.ADDRESS;

		if (codigo.charAt(0) == '9') {
			url += "/registro_presenca/registrar_ponto_android";
		} else {
			url += "/registro_presenca/registro_android";
		}

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("codigo", codigo));

			if (params.length == 2) {
				nameValuePairs.add(new BasicNameValuePair("time_millis", params[1]));
				Log.i("TIME MILLIS", params[1]);
			}

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse httpResponse = httpclient.execute(httppost);

			if (params.length == 2)
				return null;

			HttpEntity responseEntity = httpResponse.getEntity();

			String response = null;
			if (responseEntity != null) {
				response = EntityUtils.toString(responseEntity);
			}

			Log.i("=======[", response);

			String mensagens[] = response.split(";");

			System.out.println("LENGTH: " + mensagens.length);

			if (mensagens.length == 1) {
				mensagens = mensagens[0].replace("|", ";").split(";");
				System.out.println("Messages" + mensagens);
				System.out.println("Messages[0]: " + mensagens[0]);
				showMessage(mensagens[0]);
				executaMensagemSonora(mensagens[1]);
			} else {
				Intent intent = new Intent(context, RegistroPresencaActivity.class);
				intent.putExtra("SAUDACAO", mensagens[0]);
				intent.putExtra("NOME", mensagens[1]);
				intent.putExtra("FOTO", mensagens[2]);
				intent.putExtra("NOTICE", mensagens[3]);
				intent.putExtra("ERROR", mensagens[4]);
				intent.putExtra("CHEGADA", mensagens[5]);
				intent.putExtra("MENSAGEM", mensagens[6]);
				context.startActivity(intent);
			}

			return true;

		} catch (ClientProtocolException e) {
			showMessage("==[ClientProtocolEx:\n" + e.getMessage());
		} catch (IOException e) {
			showMessage("==[IOEx:\n" + e.getMessage());
		}

		return false;
	}

	private void executaMensagemSonora(String musicName) {
		if (musicName.equals("aluno_possui_presenca")) {
			MediaPlayer.create(context, R.raw.aluno_possui_presenca).start();
		} else if (musicName.equals("codigo_invalido")) {
			MediaPlayer.create(context, R.raw.codigo_invalido).start();
		}
	}

	protected void showMessage(final String value) {
		((Activity)context).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, value, Toast.LENGTH_LONG).show();
				((MagnusPresencaActivity)context).setEnabledButton(true);
			}
		});
	}
}
