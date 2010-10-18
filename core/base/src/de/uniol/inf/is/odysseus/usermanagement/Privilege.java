package de.uniol.inf.is.odysseus.usermanagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Privilege implements Serializable {

	private static final long serialVersionUID = -1623632077911032763L;
	private final int ID;
	private String privname;
	private Object object;
	private List<IUserActions> operations;

	Privilege(String privname, int newid) {
		this(privname, null, new ArrayList<IUserActions>(), newid);
	}

	Privilege(String privname, Object obj, List<IUserActions> operations,
			int newid) {
		this.privname = privname;
		this.object = obj;
		this.operations = operations;
		this.ID = newid;
	}

	Privilege(Object obj, List<IUserActions> operations, int newid) {
		this(obj.getClass().toString() + "_" + newid, obj, operations, newid);
	}

	public int getID() {
		return this.ID;
	}

	String getPrivname() {
		return this.privname;
	}

	List<IUserActions> getOperations() {
		return this.operations;
	}

	void setPrivname(String newprivname) {
		this.privname = newprivname;
	}

	void setObject(Object newobj) {
		this.object = newobj;
	}

	void addOperation(UserManagementActions newOperations) {
		this.operations.add(newOperations);
	}

	public Object getObject() {
		return this.object;
	}

	/**
	 * checks for identical ID.
	 * 
	 * @param priv
	 * @return
	 */
	public boolean equals(Privilege priv) {
		if (this.ID == priv.getID()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Privilege other = (Privilege) obj;
		if (this.ID != other.getID()) {
			return false;
		} else if (this.privname == null) {
			if (other.getPrivname() != null)
				return false;
		} else if (!this.privname.equals(other.getPrivname())) {
			return false;
		} else if (this.operations.equals(other.getOperations())) {
			return true;
		}

		return true;
	}
}