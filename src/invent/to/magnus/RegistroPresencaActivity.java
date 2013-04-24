package invent.to.magnus;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class RegistroPresencaActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro_presenca);
		String saudacao = getIntent().getExtras().getString("SAUDACAO");		
		((TextView)findViewById(R.id.saudacao)).setText(saudacao);
		
		MediaPlayer media = new MediaPlayer();
		try {
			String mensagem = getIntent().getExtras().getString("MENSAGEM");
			String url = "http://translate.google.com/translate_tts?tl=pt&q="+mensagem.replace(' ', '+').replace("!", ".");
			Uri uri = Uri.parse(url);
			media.setDataSource(this, uri);
			media.setAudioStreamType(AudioManager.STREAM_MUSIC);
			media.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!media.isPlaying()) {
			media.start();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registro_presenca, menu);
		return true;
	}

}
