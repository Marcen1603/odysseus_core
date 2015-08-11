package de.uniol.inf.is.odysseus.iql.basic.typing.factory;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLFactory;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.service.BasicIQLServiceObserver;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;

@Singleton
public class BasicIQLTypeFactory extends AbstractIQLTypeFactory<BasicIQLTypeUtils, BasicIQLServiceObserver> {

	@Inject
	public BasicIQLTypeFactory(BasicIQLTypeUtils typeUtils,BasicIQLServiceObserver serviceObserver) {
		super(typeUtils, serviceObserver);
	}

	@Override
	public String getFileExtension() {
		return "basiciql";
	}

	@Override
	protected IQLModel createCleanSystemFile() {
		return BasicIQLFactory.eINSTANCE.createIQLModel();
	}
	
}
