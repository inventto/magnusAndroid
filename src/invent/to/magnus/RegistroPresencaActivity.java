package invent.to.magnus;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class RegistroPresencaActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro_presenca);
		
		contaTempo();
		
		String urlFoto = getIntent().getExtras().getString("FOTO");
		Drawable drawable = LoadImageFromWebOperations("http://magnus.invent.to" + urlFoto);
		((ImageView)findViewById(R.id.foto)).setImageDrawable(drawable);
		
		String saudacao = getIntent().getExtras().getString("SAUDACAO");		
		((TextView)findViewById(R.id.saudacao)).setText(saudacao);
		
		((TextView)findViewById(R.id.horario)).setText("Chegada: " + new SimpleDateFormat("HH:mm").format(new GregorianCalendar().getTime()) + "\n");
		
		String notice = getIntent().getExtras().getString("NOTICE");
		((TextView)findViewById(R.id.notice)).setText(notice.replace("<br/>", "\n"));
		
		String error = getIntent().getExtras().getString("ERROR");
		((TextView)findViewById(R.id.error)).setText(error.replace("<br/>", "\n"));
/*		
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
 */
	}
		
	private void contaTempo(){
		TimerTask ttask = new TimerTask() {
			
			@Override
			public void run() {
				Intent intent = new Intent(RegistroPresencaActivity.this, MagnusPresencaActivity.class);
				startActivity(intent);
				finishScreen();
			}
		};
		
		Timer t = new Timer();
	    t.schedule(ttask, 10000);
	}
	
	private void finishScreen() {
        this.finish();
    }
	
	private Drawable LoadImageFromWebOperations(String url){
      try{
        InputStream is = (InputStream) new URL(url).getContent();
        Drawable d = Drawable.createFromStream(is, "src name");
        return d;
      }catch (Exception e) {
        System.out.println("==>LoadImageException" + e);
        return null;
      }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registro_presenca, menu);
		return true;
	}	
}
