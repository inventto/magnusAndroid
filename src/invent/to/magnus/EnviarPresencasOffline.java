package invent.to.magnus;

import invent.to.magnus.entity.Aluno;
import invent.to.magnus.helper.ORMLiteHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

public class EnviarPresencasOffline {

	private Dao<Aluno, Integer> alunoDao;

	public void enviarPresencasOffline(Context context) {
		List<Aluno> alunos = buscarAlunosLocal(context);
		for (Aluno aluno : alunos) {
			try {
				Log.i("GET DATA REGISTRO[", aluno.getDataRegistro().toString());
				Log.i("GET DATA REGISTRO VALUE OF[", String.valueOf(aluno.getDataRegistro()));
				Log.i("GET DATA REGISTRO Get Time[", String.valueOf(aluno.getDataRegistro().getTime()));
				AsyncTask<String, String, Boolean> presenca = new RespostaRegistroPresenca(context).execute(aluno.getCodigo(), String.valueOf(aluno.getDataRegistro().getTime()));
				Boolean p = presenca.get();
				if (p != null && p.booleanValue() == true) {
					Log.i(">>>>>>>>>>>> DELETE", aluno.getCodigo());
					alunoDao.delete(aluno);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private List<Aluno> buscarAlunosLocal(Context context) {
		try {
			alunoDao = ORMLiteHelper.getInstance(context).getAlunoDao();
			return alunoDao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<Aluno>();
	}

}
