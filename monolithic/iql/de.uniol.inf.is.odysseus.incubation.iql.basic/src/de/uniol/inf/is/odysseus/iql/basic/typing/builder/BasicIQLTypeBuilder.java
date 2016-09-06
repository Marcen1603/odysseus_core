package de.uniol.inf.is.odysseus.iql.basic.typing.builder;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.BasicIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;

public class BasicIQLTypeBuilder extends AbstractIQLTypeBuilder<BasicIQLTypeDictionary, BasicIQLTypeUtils> {

	@Inject
	public BasicIQLTypeBuilder(BasicIQLTypeDictionary typeDictionary, BasicIQLTypeUtils typeUtils) {
		super(typeDictionary, typeUtils);
	}
}
