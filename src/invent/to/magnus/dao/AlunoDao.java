package invent.to.magnus.dao;

import java.sql.SQLException;

import invent.to.magnus.entity.Aluno;

import com.j256.ormlite.dao.BaseDaoImpl;

public class AlunoDao extends BaseDaoImpl<Aluno, Integer>{

	protected AlunoDao(Class<Aluno> dataClass) throws SQLException {
		super(dataClass);
		setConnectionSource(connectionSource);
		initialize();
	}
	
}