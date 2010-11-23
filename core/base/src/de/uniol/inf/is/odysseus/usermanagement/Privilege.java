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

	void addOperation(IUserAction newOperation) {
		this.operations.add(newOperation);
	}

	public void addOperations(List<IUserAction> operations) {
		for (IUserAction action : operations) {
			if (!this.operations.contains(action)) {
				addOperation(action);
			}
		}
	}

	@Override
	/**
	 * Indicates whether some other object is "equal to" this one.
	 * A privilege has a final owner and only references to one object.
	 * Because of that, there can't exist another privilege with the same owner on the same object.
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.hashCode() == obj.hashCode())
			return true;
		if (getClass() != obj.getClass())
			return false;
		Privilege other = (Privilege) obj;
		if (this.owner.toString() != other.getOwner()) {
			return false;
		}
		if (this.objecturi != other.getObject()) {
			return false;
		}
		return true;
	}

	public int getID() {
		return this.ID;
	}

	/**
	 * return the object uri
	 * 
	 * @return
	 */
	public Object getObject() {
		return this.objecturi;
	}

	public List<IUserAction> getOperations() {
		return this.operations;
	}

	public String getOwner() {
		return this.owner.toString();
	}

	public String getPrivname() {
		return this.privname;
	}

	public boolean isEmpty() {
		if (this.operations.isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean ownerIsUser() {
		if (this.owner instanceof User) {
			return true;
		}
		return false;
	}

	public void removeOperation(IUserAction operation) {
		this.operations.remove(operation);
	}

	public void removeOperations(List<IUserAction> operations) {
		this.operations.removeAll(operations);
	}

	void setObject(String newobj) {
		this.objecturi = newobj;
	}

	void setPrivname(String newprivname) {
		this.privname = newprivname;
	}

	@Override
	public String toString() {
		return this.privname;
	}

}