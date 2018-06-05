package de.uniol.inf.is.odysseus.iql.basic.scoping;

import org.eclipse.xtext.common.types.JvmType;

public interface IIQLJdtTypeProvider {

	JvmType findTypeByName(String name);
	
}
