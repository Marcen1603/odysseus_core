package de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;

public class BasicIQLCompilerHelper extends AbstractIQLCompilerHelper<BasicIQLLookUp, BasicIQLTypeFactory, BasicIQLTypeUtils> {

	@Inject
	public BasicIQLCompilerHelper(BasicIQLLookUp lookUp, BasicIQLTypeFactory typeFactory, BasicIQLTypeUtils typeUtils) {
		super(lookUp,typeFactory,  typeUtils);
	}

}
