package de.uniol.inf.is.odysseus.iql.basic.typing.dictionary;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.xtext.resource.XtextResourceSet;

import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLClasspathTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.service.BasicIQLServiceObserver;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;

@Singleton
public class BasicIQLTypeDictionary extends AbstractIQLTypeDictionary<BasicIQLTypeUtils, BasicIQLServiceObserver> {

	@Inject
	public BasicIQLTypeDictionary(BasicIQLTypeUtils typeUtils,	BasicIQLServiceObserver serviceObserver,
			XtextResourceSet systemResourceSet,
			IQLClasspathTypeProviderFactory typeProviderFactory,
			IQLQualifiedNameConverter converter) {
		super(typeUtils, serviceObserver, systemResourceSet, typeProviderFactory,converter);
	}

	@Override
	public String getFileExtension() {
		return "basiciql";
	}
	
}
