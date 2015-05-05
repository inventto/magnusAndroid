package invent.to.magnus;

import invent.to.magnus.entity.Aluno;
import invent.to.magnus.helper.ORMLiteHelper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

public class MagnusPresencaActivity extends GlobalActivity {

	private Handler handler;
	private Thread thread;

	public synchronized Handler getHandler() {
		return this.handler;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (Wifi.testConnection()) {
			new EnviarPresencasOffline().enviarPresencasOffline(this);
		} else {
			Toast.makeText(this, "Não há conexão com a internet.\nAssim que a mesma for estabelecida registraremos sua presença!", Toast.LENGTH_LONG).show();
		}

		setEnabledButton(true);
		displayKeyboard();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_magnus_presenca);

		onResume();

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		if (wifiIsConnected()) {
			this.handler = new Handler();
			thread = new Thread(new MarcarFaltaRunnable(this));
			thread.start();
		}

		new VerifyVersionAndUpdate(this);

		codigoOnChanged();
	}

	private void codigoOnChanged() {
		EditText codigo = (EditText)findViewById(R.id.codigo);
		codigo.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				activity();
			}
		});
		codigo.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == 2) {
					registrar(v);
					displayKeyboard();
				}
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.magnus_presenca, menu);
		return true;
	}

	public void setEnabledButton(boolean enabled) {
		Button but = (Button)findViewById(R.id.btnRegistrar);
		but.setEnabled(enabled);
	}

	public void registrar(View v) {
		EditText et = (EditText)findViewById(R.id.codigo);
		String codigo = et.getText().toString();
		et.setText("");
		

		if (!codigo.equals("") && Long.parseLong(codigo) > 0) {
			if (Wifi.testConnection() && checkConexaoComServidor() != false) {
				setEnabledButton(false);
				new EnviarPresencasOffline().enviarPresencasOffline(this);
				new RespostaRegistroPresenca(this).execute(codigo);
			} else {
				inserirAluno(codigo);
			}
		} else {
			Toast.makeText(this, "Código do aluno inválido!", Toast.LENGTH_LONG).show();
		}
	}
	public Boolean checkConexaoComServidor(){
		try {
			URL url = new URL(GlobalActivity.ADDRESS);
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			huc.connect();
			huc.disconnect();
			return true;
		} catch (UnknownHostException e) {
			System.out.println("EXCEPTION SOCKET: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOEXCEPTION SOCKET: " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}	

	public void showInfo(View v) {
		final Dialog dialog = new Dialog(this);
		dialog.setTitle("Sobre");
		dialog.setContentView(R.layout.sobre_dialog);
		Button btOk = (Button)dialog.findViewById(R.id_dialog_sobre.btOk);
		btOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void inserirAluno(String codigo) {
		String cumprimento = null;
		String[] horario = null;
		int hora = 0;
		
		Date data = new Date();
		Aluno aluno = new Aluno();
		
		aluno.setCodigo(codigo);
		aluno.setDataRegistro(data);
		String formathorasMinutos = new SimpleDateFormat("HH:mm").format(data);
		
		horario = formathorasMinutos.split(":");
		hora = Integer.parseInt(horario[0]);
		cumprimento = getSaudacao(hora);
		
		try {
			Dao<Aluno, Integer> alunoDao = ORMLiteHelper.getInstance(this).getAlunoDao();
			alunoDao.create(aluno);
			
			Intent intent = new Intent(this, RegistroPresencaActivity.class);
			intent.putExtra("SAUDACAO", cumprimento);
			intent.putExtra("CHEGADA", formathorasMinutos);
			intent.putExtra("MENSAGEM", "sem_wifi_presenca_salva");
			intent.putExtra("NOTICE", "Presença registrada com sucesso!!");
			intent.putExtra("ERROR", getString(R.string.sem_wifi_presenca_salva));
			this.startActivity(intent);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String getSaudacao(int hora) {
		String saudacao = null;
		if(hora < 12){
			saudacao = "Bom Dia";
		} else if (hora > 12 && hora < 18) {
			saudacao = "Boa Tarde";
		} else {
			saudacao = "Boa Noite";
		}
		return saudacao;
	}

	private boolean wifiIsConnected() {
		ConnectivityManager connManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
		return connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
	}

	private void displayKeyboard() {
		final EditText codigo = (EditText)findViewById(R.id.codigo);
		codigo.postDelayed(new Runnable() {

			@Override
			public void run() {
				InputMethodManager keyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				keyboard.showSoftInput(codigo, 0);
			}
		}, 50);
	}
}