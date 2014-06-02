package invent.to.magnus;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class RegistroPresencaActivity extends GlobalActivity {
	
	private int currentTrack = 0;
	private String mensagens[];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro_presenca);
		
		voltarTelaPrincipal();
		
		String urlFoto = getIntent().getExtras().getString("FOTO");
		Drawable drawable = LoadImageFromWebOperations(GlobalActivity.ADDRESS + urlFoto);
		ImageView iv = ((ImageView)findViewById(R.id.foto));
		iv.setImageDrawable(drawable);
		
		String saudacao = getIntent().getExtras().getString("SAUDACAO");
		((TextView)findViewById(R.id.saudacao)).setText(saudacao);
		
		String chegada = getIntent().getExtras().getString("CHEGADA");
		String msgChegada = (chegada.charAt(0) == '1') ? "Chegada: " : "Saída: ";
		((TextView)findViewById(R.id.horario)).setText(msgChegada + new SimpleDateFormat("HH:mm").format(new GregorianCalendar().getTime()) + "\n");
		
		String notice = getIntent().getExtras().getString("NOTICE");
		((TextView)findViewById(R.id.notice)).setText(notice.replace("<br/>", "\n"));
		
		String error = getIntent().getExtras().getString("ERROR");
		((TextView)findViewById(R.id.error)).setText(error.replace("<br/>", "\n"));
		
		String musicName = getIntent().getExtras().getString("MENSAGEM");
		musicName = musicName.replace("|", ";");
		
		mensagens = musicName.split(";");
		executaMensagensSonora(mensagens);
	}
	
	private OnCompletionListener onCompletionListener(){
		OnCompletionListener comp = new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				currentTrack = currentTrack + 1;
				if (currentTrack < mensagens.length) {
					mp = getMusica(mensagens[currentTrack]);
					if (mp != null){
					  mp.start();
					  mp.setOnCompletionListener(onCompletionListener());
					}
				}
			}
		};
		return comp; 
	}
		
	private void executaMensagensSonora(final String[] mensagens){
		MediaPlayer mp = null;
		mp = getMusica(mensagens[currentTrack]);
		if (mp != null){
		  mp.start();
		  mp.setOnCompletionListener(onCompletionListener());
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
		if (musicName.equals("fora_de_horario")) {
			mp = MediaPlayer.create(this, R.raw.fora_de_horario_matricula);
		}
		if (musicName.equals("dia_errado")) {
			mp = MediaPlayer.create(this, R.raw.fora_do_dia_normal_de_aula);
		}
		if (musicName.equals("aula_de_reposicao")) {
			mp = MediaPlayer.create(this, R.raw.aula_de_resposicao);
		}
		if (musicName.equals("justificou_aula_passada")) {
			mp = MediaPlayer.create(this, R.raw.falta_justificada);
		}
		if (musicName.equals("bom_dia")) {
			mp = MediaPlayer.create(this, R.raw.bom_dia);
		}
		if (musicName.equals("boa_tarde")) {
			mp = MediaPlayer.create(this, R.raw.boa_tarde);
		}
		if (musicName.equals("boa_noite")) {
			mp = MediaPlayer.create(this, R.raw.boa_noite);
		}
		if (musicName.equals("tenha_boa_tarde")) {
			mp = MediaPlayer.create(this, R.raw.tenha_boa_tarde);
		}
		if (musicName.equals("tenha_boa_noite")) {
			mp = MediaPlayer.create(this, R.raw.tenha_boa_noite);
		}		
		return mp;
	}

	private void voltarTelaPrincipal(){
		new Handler().postDelayed(new Runnable() {
			public void run() {
				RegistroPresencaActivity.this.finish();
				System.gc();
			}
		}, 10000);
    }
	
	private Drawable LoadImageFromWebOperations(String url){
      try{
        InputStream is = (InputStream) new URL(url).getContent();
        Drawable d = Drawable.createFromStream(is, "src name");
        return d;
      }catch (Exception e) {
    	  Log.d("exception", e.getMessage());
      }catch (Error e1) {
    	  Log.d("error", e1.getMessage());
      }
      return null;
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registro_presenca, menu);
		return true;
	}	
}
