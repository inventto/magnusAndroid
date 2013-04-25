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
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class RegistroPresencaActivity extends Activity {
	
	private int currentTrack = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro_presenca);
		
		contaTempo();
		
		String urlFoto = getIntent().getExtras().getString("FOTO");
		Drawable drawable = LoadImageFromWebOperations("http://magnus.invent.to" + urlFoto);
		ImageView iv = ((ImageView)findViewById(R.id.foto));
		iv.setImageDrawable(drawable);
		
		String saudacao = getIntent().getExtras().getString("SAUDACAO");		
		((TextView)findViewById(R.id.saudacao)).setText(saudacao);
		
		((TextView)findViewById(R.id.horario)).setText("Chegada: " + new SimpleDateFormat("HH:mm").format(new GregorianCalendar().getTime()) + "\n");
		
		String notice = getIntent().getExtras().getString("NOTICE");
		((TextView)findViewById(R.id.notice)).setText(notice.replace("<br/>", "\n"));
		
		String error = getIntent().getExtras().getString("ERROR");
		((TextView)findViewById(R.id.error)).setText(error.replace("<br/>", "\n"));
		
		String musicName = getIntent().getExtras().getString("MENSAGEM");
		musicName = musicName.replace("|", ";");
		
		String mensagens[] = musicName.split(";");
				
		executaMensagensSonora(mensagens);
	}
	
	private void executaMensagensSonora(final String[] mensagens){
		MediaPlayer mp = null;
		try {
			mp = getMusica(mensagens[currentTrack]);
			mp.start();
			mp.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					currentTrack = (currentTrack + 1) % mensagens.length;
					mp = getMusica(mensagens[currentTrack]);
					mp.start();
				}
			});
		} catch (IllegalStateException e) {
			Log.i("==[ExecutaMsgSonExcept", e.getMessage());
			e.printStackTrace();
		}
	}
		
	private MediaPlayer getMusica(String musicName) {
		MediaPlayer mp = null;
		if (musicName.equals("aguarde_um_instante")) {
			mp = MediaPlayer.create(this, R.raw.aguarde_um_instante);
		} else if (musicName.equals("voce_esta_atrasado")){
			mp = MediaPlayer.create(this, R.raw.voce_esta_atrasado);
		}
		if (musicName.equals("bem_vindo")){
			mp = MediaPlayer.create(this, R.raw.bem_vindo);
		} 
		if (musicName.equals("hoje_nao_e_dia_normal_de_aula")){
			mp = MediaPlayer.create(this, R.raw.hoje_nao_e_dia_normal_de_aula);
		}
		if (musicName.equals("parabens_semana")){
			mp = MediaPlayer.create(this, R.raw.parabens_semana);
		} else if (musicName.equals("parabens_mes")){
			mp = MediaPlayer.create(this, R.raw.parabens_mes);
		} 
		if (musicName.equals("parabens_pontualidade")){
			mp = MediaPlayer.create(this, R.raw.parabens_pontualidade);
		}
		if (musicName.equals("voce_faltou")){
			mp = MediaPlayer.create(this, R.raw.voce_faltou);
		}
		return mp;
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
	    t.schedule(ttask, 13000);
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
