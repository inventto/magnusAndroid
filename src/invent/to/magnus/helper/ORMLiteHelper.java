package invent.to.magnus.helper;

import invent.to.magnus.entity.Aluno;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class ORMLiteHelper extends OrmLiteSqliteOpenHelper{
    private static final String DATABASE_NAME = "inventto_magnus.db";
    private static final int DATABASE_VERSION = 1;
    private static ORMLiteHelper mInstance = null;
    private Dao<Aluno, Integer> alunoDao = null;
    private RuntimeExceptionDao<Aluno, Integer> alunoRuntimeDao = null;
    
    public ORMLiteHelper(Context context, CursorFactory factory) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
            // TableUtils é responsável por algumas operações sobre tabelas,
            // como, por exemplo, deletar/inserir tabelas.
            TableUtils.createTable(connectionSource, Aluno.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {            
            TableUtils.dropTable(connectionSource, Aluno.class, true);
            onCreate(db, connectionSource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}
	
	public static ORMLiteHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ORMLiteHelper(context.getApplicationContext(), null);
        }
        return mInstance;
    }
	
	public Dao<Aluno, Integer> getAlunoDao() throws SQLException {
        if (alunoDao == null) {
            alunoDao = getDao(Aluno.class);
        }
        return alunoDao;
    }
    
	public RuntimeExceptionDao<Aluno, Integer> getAlunoRuntimeDao() {
		if (alunoRuntimeDao == null) {
			alunoRuntimeDao = getRuntimeExceptionDao(Aluno.class);
		}
		return alunoRuntimeDao;
	}
}
