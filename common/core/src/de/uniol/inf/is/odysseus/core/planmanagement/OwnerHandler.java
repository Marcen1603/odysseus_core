package de.uniol.inf.is.odysseus.core.planmanagement;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.IdentityArrayList;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class OwnerHandler implements IOwnedOperator {

	final private List<IOperatorOwner> owners;
	final private List<ISession> sessions;

	public OwnerHandler() {
		owners = new IdentityArrayList<IOperatorOwner>();
		sessions = new IdentityArrayList<ISession>();
	}

	public OwnerHandler(OwnerHandler other) {
		owners = new IdentityArrayList<IOperatorOwner>(other.owners);
		sessions = new IdentityArrayList<ISession>(other.sessions);
	}


	@Override
	public void addOwner(IOperatorOwner owner) {
		if (!this.owners.contains(owner)) {
			this.owners.add(owner);
		}
		sessions.add(owner.getSession());
		Collections.sort(owners, OperatorOwnerComparator.getInstance());
	}
	
	@Override
	public void addOwner(Collection<IOperatorOwner> owner) {
		this.owners.addAll(owner);
		for (IOperatorOwner o: owners){
			sessions.add(o.getSession());
		}
		Collections.sort(owners, OperatorOwnerComparator.getInstance());
	}

	@Override
	public void removeOwner(IOperatorOwner owner) {
		// remove all occurrences
		while (this.owners.remove(owner)) {
			sessions.remove(owner.getSession());
		}
		// synchronized (this.deactivateRequestControls) {
		// this.deactivateRequestControls.remove(owner);
		// }
		Collections.sort(owners, OperatorOwnerComparator.getInstance());
	}

	@Override
	public void removeAllOwners() {
		this.owners.clear();
		this.sessions.clear();
	}

	@Override
	public boolean isOwnedBy(IOperatorOwner owner) {
		return this.owners.contains(owner);
	}

	@Override
	public boolean isOwnedByAny(List<IOperatorOwner> owners) {
		for (IOperatorOwner o:owners){
			if (isOwnedBy(o)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isOwnedByAll(List<IOperatorOwner> owners){
		return this.owners.containsAll(owners);
	}

	
	@Override
	public boolean hasOwner() {
		return !this.owners.isEmpty();
	}

	@Override
	public List<IOperatorOwner> getOwner() {
		return Collections.unmodifiableList(this.owners);
	}

	/**
	 * Returns a ","-separated string of the owner IDs.
	 * 
	 * @param owner
	 *            Owner which have IDs.
	 * @return ","-separated string of the owner IDs.
	 */
	@Override
	public String getOwnerIDs() {
		StringBuffer result = new StringBuffer();
		for (IOperatorOwner iOperatorOwner : owners) {
			if (result.length() > 0) {
				result.append(", ");
			}
			result.append(iOperatorOwner.getID());
		}
		return result.toString();
	}

	public List<ISession> getSessions() {
		return Collections.unmodifiableList(sessions);
	}

	
}
