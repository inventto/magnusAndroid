package invent.to.magnus;

import invent.to.magnus.entity.Aluno;
import invent.to.magnus.helper.ORMLiteHelper;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class NetworkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		final ConnectivityManager connMgr = (ConnectivityManager)arg0.getSystemService(Context.CONNECTIVITY_SERVICE);
		final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifi.isAvailable()) {
			verifAlunosRegistrados(arg0);
		}
	}
	
	private static void verifAlunosRegistrados(Context context) {
		try {
			Dao<Aluno, Integer> alunoDao = ORMLiteHelper.getInstance(context).getAlunoDao();
			List<Aluno> listAluno = alunoDao.queryForAll();
			for(Aluno aluno : listAluno){
				new RespostaRegistroPresenca(context).execute(aluno.getCodigo(), String.valueOf(aluno.getDataRegistro().getTime()));
				alunoDao.delete(aluno);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
