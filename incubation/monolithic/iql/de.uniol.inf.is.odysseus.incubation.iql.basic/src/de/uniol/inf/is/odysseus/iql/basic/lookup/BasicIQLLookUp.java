package de.uniol.inf.is.odysseus.iql.basic.lookup;



import javax.inject.Inject;





import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.BasicIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.BasicIQLTypeExtensionsDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;

public class BasicIQLLookUp extends AbstractIQLLookUp<BasicIQLTypeDictionary, BasicIQLTypeExtensionsDictionary, BasicIQLTypeUtils>{

	@Inject
	public BasicIQLLookUp(BasicIQLTypeDictionary typeDictionary,BasicIQLTypeExtensionsDictionary typeExtensionsDictionary, BasicIQLTypeUtils typeUtils) {
		super(typeDictionary, typeExtensionsDictionary, typeUtils);
	}


}
