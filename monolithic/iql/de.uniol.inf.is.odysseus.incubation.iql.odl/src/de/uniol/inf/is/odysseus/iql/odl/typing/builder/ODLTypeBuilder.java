package de.uniol.inf.is.odysseus.iql.odl.typing.builder;

import javax.inject.Inject;


import de.uniol.inf.is.odysseus.iql.basic.typing.builder.AbstractIQLTypeBuilder;
import de.uniol.inf.is.odysseus.iql.odl.typing.dictionary.IODLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;

public class ODLTypeBuilder extends AbstractIQLTypeBuilder<IODLTypeDictionary, IODLTypeUtils> implements IODLTypeBuilder {

	@Inject
	public ODLTypeBuilder(IODLTypeDictionary typeDictionary, IODLTypeUtils typeUtils) {
		super(typeDictionary, typeUtils);
	}
	

}
