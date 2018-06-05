package de.uniol.inf.is.odysseus.core.physicaloperator;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.metadata.ITimeComparable;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface IPunctuation extends ITimeComparable, IStreamable, Serializable {

	/**
	 * Every punctuation needs a time to allow the ordering regarding the stream
	 * @return
	 */
	PointInTime getTime();
	
	/**
	 * States that this puncutation is a heartbeat (i.e. states time progress). This is used to avoid
	 * instanceof calls at runtime. If the element returns true, sequent punctuations may compress (i.e.
	 * only the youngest punctuation is send)
	 * @return
	 */
	boolean isHeartbeat();
	
	byte getNumber();
	
	/**
	 * 
	 * @return
	 */
	SDFSchema getSchema();
	
	Tuple<?> getValue();
		
	IPunctuation clone();

	IPunctuation clone(PointInTime p_start);
	
}
