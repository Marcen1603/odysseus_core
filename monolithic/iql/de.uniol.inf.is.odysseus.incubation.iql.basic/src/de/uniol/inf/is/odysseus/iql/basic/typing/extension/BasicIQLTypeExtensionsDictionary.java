package de.uniol.inf.is.odysseus.iql.basic.typing.extension;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.BasicIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;

@Singleton
public class BasicIQLTypeExtensionsDictionary extends AbstractIQLTypeExtensionsDictionary<BasicIQLTypeDictionary, BasicIQLTypeUtils> {

	@Inject
	public BasicIQLTypeExtensionsDictionary(BasicIQLTypeDictionary typeDictionary, BasicIQLTypeUtils typeUtils) {
		super(typeDictionary, typeUtils);
	}

}
