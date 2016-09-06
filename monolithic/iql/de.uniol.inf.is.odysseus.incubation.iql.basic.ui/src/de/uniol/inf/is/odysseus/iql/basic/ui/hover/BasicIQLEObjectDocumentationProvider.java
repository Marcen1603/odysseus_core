package de.uniol.inf.is.odysseus.iql.basic.ui.hover;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.BasicIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;

public class BasicIQLEObjectDocumentationProvider extends AbstractIQLEObjectDocumentationProvider<BasicIQLTypeDictionary, BasicIQLTypeUtils> {

	@Inject
	public BasicIQLEObjectDocumentationProvider(BasicIQLTypeDictionary typeDictionary, BasicIQLTypeUtils typeUtils) {
		super(typeDictionary, typeUtils);
	}

}
