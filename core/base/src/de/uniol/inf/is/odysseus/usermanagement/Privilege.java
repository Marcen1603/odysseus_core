package de.uniol.inf.is.odysseus.usermanagement;

import java.io.Serializable;
import java.util.List;

/**
 * Privileges stores actions (permissions). A Privilege is owned by one user or
 * role and identified by the object name.
 * 
 * @see de.uniol.inf.is.odysseus.usermanagement.IUserAction.java
 * @author Christian van Göns
 * 
 */
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

	/**
	 * Adds a new action (permissions) to a privilege.
	 * 
	 * @param newOperation
	 */
	void addOperation(IUserAction newOperation) {
		this.operations.add(newOperation);
	}

	/**
	 * Adds a list of new actions (permissions) to a privilege.
	 * 
	 * @param operations
	 */
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

	/**
	 * Returns a privileges id.
	 * 
	 * @return int
	 */
	public int getID() {
		return this.ID;
	}

	/**
	 * return the objects uri (name).
	 * 
	 * @return String
	 */
	public String getObject() {
		return this.objecturi;
	}

	/**
	 * Returns a list of all actions (permissions) contained in a privilege.
	 * 
	 * @return List<IUserAction>
	 */
	public List<IUserAction> getOperations() {
		return this.operations;
	}

	/**
	 * Returns the owner of the privilege. The owner can be a user or role. To
	 * check if the owner is a user call the method <b>ownerIsUser</b>.
	 * 
	 * @return String
	 */
	public String getOwner() {
		return this.owner.toString();
	}

	/**
	 * Returns the name of the privilege.
	 * 
	 * @return String
	 */
	public String getPrivname() {
		return this.privname;
	}

	/**
	 * Returns whether the privilege has actions or not.
	 * 
	 * @return boolean
	 */
	public boolean isEmpty() {
		if (this.operations.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Returns whether the owner is a instance of a user.
	 * 
	 * @return boolean
	 */
	public boolean ownerIsUser() {
		if (this.owner instanceof User) {
			return true;
		}
		return false;
	}

	/**
	 * Removes a action (permissions) from a privilege.
	 * 
	 * @param operation
	 */
	public void removeOperation(IUserAction operation) {
		this.operations.remove(operation);
	}

	/**
	 * Removes a list of actions (permissions) from a privilege.
	 * 
	 * @param operations
	 */
	public void removeOperations(List<IUserAction> operations) {
		this.operations.removeAll(operations);
	}

	/**
	 * Sets a new object uri the action actions (permissions) in a privilege are
	 * for.
	 * 
	 * @param object
	 */
	void setObject(String newobjecturi) {
		this.objecturi = newobjecturi;
	}

	/**
	 * Sets a new privilege name.
	 * 
	 * @param newprivname
	 */
	void setPrivname(String newprivname) {
		this.privname = newprivname;
	}

	@Override
	public String toString() {
		return this.privname;
	}

}