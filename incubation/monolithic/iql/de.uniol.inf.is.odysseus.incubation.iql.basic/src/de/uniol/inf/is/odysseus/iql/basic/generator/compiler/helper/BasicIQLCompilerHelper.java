package de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper;

import javax.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.lookup.BasicIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.BasicIQLTypeFactory;

public class BasicIQLCompilerHelper extends AbstractIQLCompilerHelper<BasicIQLLookUp, BasicIQLTypeFactory> {

	@Inject
	public BasicIQLCompilerHelper(BasicIQLLookUp lookUp, BasicIQLTypeFactory factory) {
		super(lookUp, factory);
	}

}
