package de.uniol.inf.is.odysseus.iql.odl.ui.hover;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.ui.hover.AbstractIQLEObjectDocumentationProvider;
import de.uniol.inf.is.odysseus.iql.odl.typing.dictionary.IODLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;

public class ODLEObjectDocumentationProvider extends AbstractIQLEObjectDocumentationProvider<IODLTypeDictionary, IODLTypeUtils> {

	@Inject
	public ODLEObjectDocumentationProvider(IODLTypeDictionary typeDictionary, IODLTypeUtils typeUtils) {
		super(typeDictionary, typeUtils);
	}

}
