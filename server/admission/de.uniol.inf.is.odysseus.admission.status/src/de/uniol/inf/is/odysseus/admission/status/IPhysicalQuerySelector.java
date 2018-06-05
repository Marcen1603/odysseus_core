package de.uniol.inf.is.odysseus.admission.status;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public interface IPhysicalQuerySelector {

	public boolean isSelected( IPhysicalQuery query );
	
}
