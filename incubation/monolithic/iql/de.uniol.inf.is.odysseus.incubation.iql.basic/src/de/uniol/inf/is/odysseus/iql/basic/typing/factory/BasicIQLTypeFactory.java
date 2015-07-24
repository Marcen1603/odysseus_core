package de.uniol.inf.is.odysseus.iql.basic.typing.factory;

import javax.inject.Singleton;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLFactory;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile;

@Singleton
public class BasicIQLTypeFactory extends AbstractIQLTypeFactory {

	@Override
	public String getFileExtension() {
		return "basiciql";
	}

	@Override
	protected IQLFile createCleanSystemFile() {
		return BasicIQLFactory.eINSTANCE.createIQLFile();
	}
	
}
