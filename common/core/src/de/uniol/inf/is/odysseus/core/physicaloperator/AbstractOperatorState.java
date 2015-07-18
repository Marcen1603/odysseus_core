package de.uniol.inf.is.odysseus.core.physicaloperator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public abstract class AbstractOperatorState implements IOperatorState, Serializable {

	/***
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractOperatorState.class);
	
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
			size += getSizeOfSimpleDatatypeInBytes(type);
			
		}
		return size;
	} 
	
	protected boolean isSimpleDataType(SDFAttribute attribute) {
		
		SDFDatatype type = attribute.getDatatype();
		if(type.isDouble() || type.isFloat() || type.isLong() || type.isEndTimestamp() || type.isStartTimestamp() || type.isTimestamp() || type.isInteger()) {
			return true;
		}
		return false;
	}
	
	protected int getSizeOfSimpleDataAttributeInBytes(SDFAttribute attr) {
		
		SDFDatatype type = attr.getDatatype();
		if (type.isDouble() || type.isFloat() || type.isLong() || type.isEndTimestamp() || type.isStartTimestamp() || type.isTimestamp())
			return 8;
		if(type.isInteger())
			return 4;
		//Default? return 4 bytes.
		return 0;
	}
	
	protected int getSizeOfSimpleDatatypeInBytes(SDFDatatype type) {
		if (type.isDouble() || type.isFloat() || type.isLong() || type.isEndTimestamp() || type.isStartTimestamp() || type.isTimestamp())
			return 8;
		if(type.isInteger())
			return 4;
		//Default? return 4 bytes.
		return 0;
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
	
	protected int getSizeInBytesOfSerializable(Serializable obj) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream out = new ObjectOutputStream(baos);
			out.writeObject(obj);
			out.flush();
			out.close();
		} catch (IOException e) {
			LOG.error("Could not serialize Streamable. Returning 0");
			return 0;
		}
		//Sub 4 Bytes for Serialization magic Numbers
		int objectSize = baos.toByteArray().length - 4;
		return objectSize;
	}
	
	
}
