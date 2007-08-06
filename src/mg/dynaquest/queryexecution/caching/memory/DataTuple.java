/**
 * 
 */
package mg.dynaquest.queryexecution.caching.memory;


/**
 * Hilfsklasse zur Verwaltung von Inhalten semantischer Regionen
 * 
 * @author Tobias Hesselmann
 *
 */

public class DataTuple{
	private Object id;
	private Object key;
	private Object value;
	private Object datatype;

	/* Dieses Objekt muss durch den public Konstruktor initialisiert werden */
	private DataTuple(){
	}

	public DataTuple(Object id, Object key, Object value, Object datatype){
		this.id = id;
		this.key = key;
		this.value = value;
		this.datatype = datatype;
	}

	public String toString() {
		return getId() + "|" + getKey() + "|" + getValue() + "|" + getDatatype();
	}

	public Object getId() {
		return id;
	}

	public Object getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

	public Object getDatatype() {
		return datatype;		
	}
	
}
