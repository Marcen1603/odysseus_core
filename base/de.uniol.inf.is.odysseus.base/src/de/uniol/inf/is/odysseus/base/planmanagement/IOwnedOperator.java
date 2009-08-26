package de.uniol.inf.is.odysseus.base.planmanagement;

import java.util.ArrayList;

public interface IOwnedOperator {
	public void addOwner(IOperatorOwner owner);

	public void removeOwner(IOperatorOwner owner);

	public boolean isOwnedBy(IOperatorOwner owner);

	public boolean hasOwner();

	public ArrayList<IOperatorOwner> getOwner();
}
