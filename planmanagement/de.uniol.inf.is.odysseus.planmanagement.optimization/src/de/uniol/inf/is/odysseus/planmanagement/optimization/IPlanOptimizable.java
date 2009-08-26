package de.uniol.inf.is.odysseus.planmanagement.optimization;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;

public interface IPlanOptimizable {
	public ICompiler getCompiler();

	public ArrayList<IEditableQuery> getRegisteredQueries();
}
