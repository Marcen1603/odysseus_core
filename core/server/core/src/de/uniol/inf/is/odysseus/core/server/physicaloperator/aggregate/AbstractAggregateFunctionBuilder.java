package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

abstract public class AbstractAggregateFunctionBuilder implements IAggregateFunctionBuilder {

	@Override
	public 	SDFDatatype getDatatype(String functionName, List<SDFAttribute> input){
		return null;
	}
	
}
