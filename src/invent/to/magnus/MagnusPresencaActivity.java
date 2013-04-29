package invent.to.magnus;

import invent.to.magnus.entity.Aluno;
import invent.to.magnus.helper.ORMLiteHelper;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

public class MagnusPresencaActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_magnus_presenca);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.magnus_presenca, menu);
		return true;	
	}
	
	public void registrar(View v) {
		EditText et = (EditText) findViewById(R.id.codigo);
		String codigo = et.getText().toString();
		
		if (!codigo.equals("") && Integer.parseInt(codigo) > 0) {
			if (wifiIsConnected()) {
				verifAlunosRegistrados();
				new RespostaRegistroPresenca(this).execute(codigo);	
			} else {
				inserirAluno(Integer.parseInt(codigo));
			}
		} else {
			Toast.makeText(this, "Código do aluno inválido!", Toast.LENGTH_LONG).show();
		}
	}

	private void verifAlunosRegistrados() {
		try {
			Dao<Aluno, Integer> alunoDao = ORMLiteHelper.getInstance(this).getAlunoDao();
			List<Aluno> listAluno = alunoDao.queryForAll();
			for(Aluno aluno : listAluno){
				new RespostaRegistroPresenca(this).execute(String.valueOf(aluno.getCodigo()), String.valueOf(aluno.getDataRegistro().getTime()));
				alunoDao.delete(aluno);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void inserirAluno(int codigo) {
		Aluno aluno = new Aluno();
		aluno.setCodigo(codigo);
		aluno.setDataRegistro(new Date());
		try {
			Dao<Aluno, Integer> alunoDao = ORMLiteHelper.getInstance(this).getAlunoDao();
			alunoDao.create(aluno);
			Toast.makeText(this, "Não há conexão com a internet.\nAssim que a mesma for estabelecida registraremos sua presença!", Toast.LENGTH_LONG).show();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private boolean wifiIsConnected() {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		return connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();				
	}
	
	/* Exemplos:
	 * Lugar lugar = new lugar();
	lugar.nome = "Aracaju";
	// INSERT INTO lugar VALUES ('Aracaju'); 	
	lugarDao.create(lugar);
	lugar.nome = "Sergipe";
	// UPDATE lugar SET nome = 'Sergipe' WHERE id = 1;
	lugarDao.update(lugar);
	// DELETE FROM lugar WHERE id = 1;
	lugarDao.delete(lugar);
	// SELECT * FROM lugar;
	lugarDao.queryForAll();
	// SELECT * FROM Lugar where id = lugar.id;
	lugarDao.queryForId(lugar);
	*/
}