package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.io.Serializable;
import java.util.Map;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

/**
 * @author Jonas Jacobi
 *
 */
public interface IAttributeResolver extends Serializable {

	public SDFAttribute getAttribute(String name) throws AmgigiousAttributeException, NoSuchAttributeException;
	public void updateAfterClone(Map<ILogicalOperator, ILogicalOperator> updated);
	public IAttributeResolver clone() ;
	
}