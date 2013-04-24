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
import android.os.Bundle;
import android.util.Log;
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
		String mensagens[] = musicName.split("|");
		for (int i = 0; i < mensagens.length; i++) {
			Log.i("==>MDG",mensagens[i]);
			executaMensagemSonora(mensagens[i]);	
		}
	}
		
	private void executaMensagemSonora(String musicName){
		if (musicName.equals("aguarde_um_instante")) {
			MediaPlayer.create(this, R.raw.aguarde_um_instante).start();
		} else if (musicName.equals("voce_esta_atrasado")){
			MediaPlayer.create(this, R.raw.voce_esta_atrasado).start();
		}
		if (musicName.equals("aluno_possui_presenca")){
			MediaPlayer.create(this, R.raw.aluno_possui_presenca).start();
		} 
		if (musicName.equals("bem_vindo")){
			MediaPlayer.create(this, R.raw.bem_vindo).start();
		} 
		if (musicName.equals("codigo_invalido")){
			MediaPlayer.create(this, R.raw.codigo_invalido).start();
		}
		if (musicName.equals("hoje_nao_e_dia_normal_de_aula")){
			MediaPlayer.create(this, R.raw.hoje_nao_e_dia_normal_de_aula).start();
		}
		if (musicName.equals("parabens_semana")){
			MediaPlayer.create(this, R.raw.parabens_semana).start();
		} else if (musicName.equals("parabens_mes")){
			MediaPlayer.create(this, R.raw.parabens_mes).start();
		} 
		if (musicName.equals("parabens_pontualidade")){
			MediaPlayer.create(this, R.raw.parabens_pontualidade).start();
		}
		if (musicName.equals("voce_faltou")){
			MediaPlayer.create(this, R.raw.voce_faltou).start();
		}
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
	    t.schedule(ttask, 15000);
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
