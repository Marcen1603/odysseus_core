package de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.BasicIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;

public class BasicIQLCompilerHelper extends AbstractIQLCompilerHelper<BasicIQLLookUp, BasicIQLTypeDictionary, BasicIQLTypeUtils> {

	@Inject
	public BasicIQLCompilerHelper(BasicIQLLookUp lookUp, BasicIQLTypeDictionary typeDictionary, BasicIQLTypeUtils typeUtils) {
		super(lookUp,typeDictionary,  typeUtils);
	}

}
