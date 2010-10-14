package de.uniol.inf.is.odysseus.usermanagement;

import java.io.Serializable;
import java.util.List;

public class Privilege implements Serializable {

	private static final long serialVersionUID = -1623632077911032763L;
	private int ID = 0;
	private String privname;
	private Object object;
	private List<Enum> operations;

	Privilege(String privname) {
		this.privname = privname;
		// auto id implementieren
		// this.ID = getNewID();
	}

	Privilege(String privname, Object obj, List<Enum> operations) {
		this.privname = privname;
		this.object = obj;
		this.operations = operations;
	}

	Privilege(Object obj, List<Enum> operations) {
		this.privname = obj.getClass().toString() + obj.toString();
		this.object = obj;
		this.operations = operations;
	}

	public int getID() {
		return this.ID;
	}

	String getPrivname() {
		return this.privname;
	}

	List<Enum> getOperations() {
		return this.operations;
	}

	void setPrivname(String newprivname) {
		this.privname = newprivname;
	}

	void setObject(Object newobj) {
		this.object = newobj;
	}

	void addOperation(AccessOperationEnum newenum) {
		this.operations.add(newenum);
	}

	public Object getObject() {
		return this.object;
	}

	public boolean equals(Privilege priv) {
		if (this.getObject().equals(priv.getObject())) {
			if (this.getOperations().equals(priv.getOperations())) {
				return true;
			}
		}
		return false;
	}
}