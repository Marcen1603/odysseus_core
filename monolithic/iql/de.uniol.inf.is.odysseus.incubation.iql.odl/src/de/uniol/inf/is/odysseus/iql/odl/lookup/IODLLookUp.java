package de.uniol.inf.is.odysseus.iql.odl.lookup;

import java.util.Collection;

import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;

public interface IODLLookUp extends IIQLLookUp{
	Collection<String> getOperatorMetadataKeys();

	Collection<String> getParameterMetadataKeys();

	OutputMode[] getOutputModeValues();


	boolean isClonable(JvmTypeReference typeRef);
		
	Collection<String> getParameterMetadataValues(String key);

	Collection<String> getOperatorMetadataValues(String key);

}
