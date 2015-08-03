package de.uniol.inf.is.odysseus.iql.basic.typing.factory;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLFactory;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;

@Singleton
public class BasicIQLTypeFactory extends AbstractIQLTypeFactory<BasicIQLTypeUtils> {

	@Inject
	public BasicIQLTypeFactory(BasicIQLTypeUtils typeUtils) {
		super(typeUtils);
	}

	@Override
	public String getFileExtension() {
		return "basiciql";
	}

	@Override
	protected IQLFile createCleanSystemFile() {
		return BasicIQLFactory.eINSTANCE.createIQLFile();
	}
	
}
