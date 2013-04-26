package invent.to.magnus.entity;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "alunos")
public class Aluno {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(canBeNull = false)
	private int codigo;
	@DatabaseField(canBeNull = false)
	private Date dataRegistro;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCodigo() {
		return codigo;
	}
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Date getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}
	
}
