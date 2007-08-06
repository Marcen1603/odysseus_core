/**
 * 
 */
package mg.dynaquest.queryexecution.caching.memory;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;

import mg.dynaquest.queryexecution.caching.StorageManager;
import mg.dynaquest.sourcedescription.sdf.description.SDFSource;

/**
 * Diese Klasse kapselt eine semantische Region als Objekt. Um Ressourcen zu
 * sparen, werden benötigte Werte "lazy" aus der Datenbank geladen.
 * 
 * @author Tobias Hesselmann
 * 
 */
public class SemanticRegion {

	private ConstraintFormula constraintFormula = null;

	private SDFSource source = null;

	private ArrayList<DataTuple> dataTuples = null;

	private Timestamp timeStamp = null;

	private int replacementValue = 0;

	private int id = -1;

	public SemanticRegion(ConstraintFormula constraintFormula,
			SDFSource source, ArrayList<DataTuple> dataTuples) {
		this.constraintFormula = constraintFormula;
		this.source = source;
		this.dataTuples = dataTuples;
	}

	public SemanticRegion(int id) {
		setId(id);
	}

	public SemanticRegion(SemanticRegion sr) {
		try {
			this.id = sr.getId();
			this.dataTuples = sr.getDataTuples();
			this.replacementValue = sr.getReplacementValue();
			this.source = sr.getSource();
			this.timeStamp = sr.getTimeStamp();
			this.constraintFormula = sr.getConstraintFormula();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int hashCode() {
		if (this.id != -1) {
			return this.id;
		} else
			return -1;
	}

	public boolean equals(Object o) {
		if (o instanceof SemanticRegion) {
			SemanticRegion sr = (SemanticRegion) o;
			if (this.getId() == sr.getId()) {
				return true;
			}
		}
		return false;
	}

	public int size() {
		HashSet<Integer> dataTupleIds = new HashSet<Integer>();
		
		for (DataTuple d : this.getDataTuples()) {
			dataTupleIds.add((Integer)d.getId());
		}
		return dataTupleIds.size();
	}
	
	/**
	 * Speichert semantische Region in der Datenbank
	 * @throws SQLException
	 */
	public void save() throws SQLException {
		StorageManager.getInstance().saveSemanticRegion(this);
	}

	public ConstraintFormula getConstraintFormula()
			throws IllegalArgumentException, SQLException {
		if (constraintFormula == null) {
			setConstraintFormula(StorageManager.getInstance()
					.getConstraintFormula(this));
		}
		return this.constraintFormula;
	}

	public void setConstraintFormula(ConstraintFormula constraintFormula) {
		this.constraintFormula = constraintFormula;
	}

	public void updateConstraintFormula() {
		StorageManager.getInstance().updateConstraintFormula(this);
	}
	
	public ArrayList<DataTuple> getDataTuples() {
		return dataTuples;
	}

	public void setDataTuples(ArrayList<DataTuple> dataTuples) {
		this.dataTuples = dataTuples;
	}

	public int getReplacementValue() {
		return replacementValue;
	}

	public void setReplacementValue(int replacementValue) {
		this.replacementValue = replacementValue;
	}

	/**
	 * 
	 * @return Datenquelle, aus der Inhalte der semantischen Region stammen
	 */
	public SDFSource getSource() {
		if (this.source == null) {
			this.source = StorageManager.getInstance().getSourceForRegion(this);
		}
		return source;
	}

	public void setSource(SDFSource source) {
		this.source = source;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * Überschreibt Timestamp der sem. Region im Cache Memory mit der aktuellen Systemzeit  
	 * @param timeStamp 
	 *
	 */
	public void updateTimeStamp(String timeStamp) {
		StorageManager.getInstance().updateRegionTimeStamp(this, timeStamp);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return true, wenn Inhalte dieser semantischen Region konsistent sind, false sonst
	 */
	public boolean isValid() {
		return false;
	}

	/**
	 * Entfernt semantische Region aus der Datenbank
	 * 
	 */
	public void delete() {
		try {
			StorageManager.getInstance().deleteSemanticRegion(this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
