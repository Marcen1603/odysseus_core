package de.uniol.inf.is.odysseus.planmanagement;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.usermanagement.User;

public interface ITransformation {
	public ArrayList<IPhysicalOperator> transform(ILogicalOperator op, TransformationConfiguration config, User caller, IDataDictionary dd) throws TransformationException;
}
