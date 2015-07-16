package de.uniol.inf.is.odysseus.core.physicaloperator;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public abstract class AbstractOperatorState implements IOperatorState, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6985394656242726017L;

	@Override
	public Serializable getSerializedState() {
		return this;
	}
	

	protected int getSizeOfSimpleSchemaInBytes(SDFSchema schema) {
		if (schema == null)
			return 0;

		int size = 0;
		for (SDFAttribute attribute : schema) {

			SDFDatatype type = attribute.getDatatype();
			if (type.isDouble())
				size += 8;
			else if (type.isFloat())
				size += 8;
			else if (type.isInteger())
				size += 4;
			else if (type.isLong())
				size += 8;
			else if (type.isEndTimestamp())
				size += 8;
			else if (type.isStartTimestamp())
				size += 8;
			else if (type.isTimestamp())
				size += 8;
			else
				size += 4; // default
		}

		return size;

	} 

	protected boolean hasStringOrListOrComplexDatatypes(SDFSchema schema) {
		if(schema==null) {
			return false;
		}
		
		for(SDFAttribute attribute : schema) {
			SDFDatatype type = attribute.getDatatype();
			if(type.isString() || type.isListValue() || type.isComplex()) {
				return true;
			}
			
		}
		return false;
	}
	
}
