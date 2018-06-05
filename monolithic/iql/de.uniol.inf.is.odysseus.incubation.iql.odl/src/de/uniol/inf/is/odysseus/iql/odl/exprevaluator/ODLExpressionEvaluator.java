package de.uniol.inf.is.odysseus.iql.odl.exprevaluator;

import javax.inject.Inject;


import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.AbstractIQLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.odl.lookup.IODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.typing.dictionary.IODLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.odl.typing.typeextension.IODLTypeExtensionsDictionary;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;

public class ODLExpressionEvaluator extends AbstractIQLExpressionEvaluator<IODLTypeDictionary, IODLLookUp, IODLExpressionEvaluatorContext, IODLTypeUtils, IODLTypeExtensionsDictionary> implements IODLExpressionEvaluator{

	@Inject
	public ODLExpressionEvaluator(IODLTypeDictionary typeDictionary, IODLLookUp lookUp, IODLExpressionEvaluatorContext context,IODLTypeUtils typeUtils, IODLTypeExtensionsDictionary typeExtensionsDictionary) {
		super(typeDictionary, lookUp, context, typeUtils, typeExtensionsDictionary);
	}

	
}
