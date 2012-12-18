package de.uniol.inf.is.odysseus.core.planmanagement;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import sun.awt.util.IdentityArrayList;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.IOwnedOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.OperatorOwnerComparator;

public class OwnerHandler implements IOwnedOperator {

	final private List<IOperatorOwner> owners;

	public OwnerHandler() {
		owners = new IdentityArrayList<IOperatorOwner>();
	}

	public OwnerHandler(OwnerHandler other) {
		owners = new IdentityArrayList<IOperatorOwner>(other.owners);
	}


	@Override
	public void addOwner(IOperatorOwner owner) {
		if (!this.owners.contains(owner)) {
			this.owners.add(owner);
		}
		Collections.sort(owners, OperatorOwnerComparator.getInstance());
	}
	
	@Override
	public void addOwner(Collection<IOperatorOwner> owner) {
		this.owners.addAll(owner);
		Collections.sort(owners, OperatorOwnerComparator.getInstance());
	}

	@Override
	public void removeOwner(IOperatorOwner owner) {
		// remove all occurrences
		while (this.owners.remove(owner)) {
		}
		// synchronized (this.deactivateRequestControls) {
		// this.deactivateRequestControls.remove(owner);
		// }
		Collections.sort(owners, OperatorOwnerComparator.getInstance());
	}

	@Override
	public void removeAllOwners() {
		this.owners.clear();
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

	
}
