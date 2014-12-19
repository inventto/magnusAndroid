package invent.to.magnus;

import invent.to.magnus.entity.Aluno;
import invent.to.magnus.helper.ORMLiteHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

public class EnviarPresencasOffline {

	private Dao<Aluno, Integer> alunoDao;

	public void enviarPresencasOffline(Context context) {
		List<Aluno> alunos = buscarAlunosLocal(context);
		try {
			for (Aluno aluno : alunos) {
				new RespostaRegistroPresenca(context).execute(aluno.getCodigo(), String.valueOf(aluno.getDataRegistro().getTime()));
				alunoDao.delete(aluno);
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
