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
				AsyncTask<String, String, Boolean> presenca = new RespostaRegistroPresenca(context).execute(aluno.getCodigo(), String.valueOf(aluno.getDataRegistro().getTime()));
				Boolean p = presenca.get();
				if (p != null && p.booleanValue()) {
					alunoDao.delete(aluno);
				} else {
					Log.e("ERROR >>>>>>>", aluno.getCodigo());
				}
			} catch (Exception e) {
				Log.e("ERROR >>>>>>>", aluno.getCodigo() + "-" + e.getMessage());
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
