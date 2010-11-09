package de.uniol.inf.is.odysseus.usermanagement;

import java.io.Serializable;
import java.util.List;

public final class Privilege implements Serializable {

	private static final long serialVersionUID = -1623632077911032763L;
	private final int ID;
	private String privname;
	private final AbstractUserManagementEntity owner;
	private String objecturi;
	private List<IUserAction> operations;

	Privilege(String objecturi, AbstractUserManagementEntity owner,
			List<IUserAction> operation, int privid) {
		this.ID = privid;
		this.objecturi = objecturi;
		this.operations = operation;
		this.owner = owner;
		this.privname = owner + "::" + objecturi;
	}

	public int getID() {
		return this.ID;
	}

	public String getPrivname() {
		return this.privname;
	}

	public String getOwner() {
		return this.owner.toString();
	}

	public boolean ownerIsUser() {
		if (this.owner instanceof User) {
			return true;
		}
		return false;
	}

	public List<IUserAction> getOperations() {
		return this.operations;
	}

	void setPrivname(String newprivname) {
		this.privname = newprivname;
	}

	void setObject(String newobj) {
		this.objecturi = newobj;
	}

	public void addOperations(List<IUserAction> operations) {
		for (IUserAction action : operations) {
			if (!this.operations.contains(action)) {
				addOperation(action);
			}
		}
	}

	void addOperation(IUserAction newOperation) {
		this.operations.add(newOperation);
	}

	public void removeOperations(List<IUserAction> operations) {
		this.operations.removeAll(operations);
	}

	public void removeOperation(IUserAction operation) {
		this.operations.remove(operation);
	}

	/**
	 * return the object uri
	 * 
	 * @return
	 */
	public Object getObject() {
		return this.objecturi;
	}

	@Override
	public String toString() {
		return this.privname;
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
		if (this.objecturi != other.getObject()) {
			return false;
		}

		return true;
	}

	public boolean isEmpty() {
		if (this.operations.isEmpty()) {
			return true;
		}
		return false;
	}

}