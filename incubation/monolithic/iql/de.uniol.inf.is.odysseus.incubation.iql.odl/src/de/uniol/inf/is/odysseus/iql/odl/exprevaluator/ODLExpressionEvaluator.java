package de.uniol.inf.is.odysseus.iql.odl.exprevaluator;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSuperExpression;
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.AbstractIQLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.basic.typing.TypeResult;
import de.uniol.inf.is.odysseus.iql.odl.lookup.IODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.typing.factory.IODLTypeFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.typeextension.IODLTypeExtensionsFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;

public class ODLExpressionEvaluator extends AbstractIQLExpressionEvaluator<IODLTypeFactory, IODLLookUp, IODLExpressionEvaluatorContext, IODLTypeUtils, IODLTypeExtensionsFactory> implements IODLExpressionEvaluator{

	@Inject
	public ODLExpressionEvaluator(IODLTypeFactory typeFactory, IODLLookUp lookUp, IODLExpressionEvaluatorContext context,IODLTypeUtils typeUtils, IODLTypeExtensionsFactory typeExtensionsFactory) {
		super(typeFactory, lookUp, context, typeUtils, typeExtensionsFactory);
	}
	
	@Override
	public TypeResult getType(IQLSuperExpression expr, IODLExpressionEvaluatorContext context) {
		ODLOperator operator = EcoreUtil2.getContainerOfType(expr, ODLOperator.class);
		if (operator != null) {
			return getSuperType(expr);
		} else {
			return super.getType(expr, context);
		}
	}
	
	@Override
	public TypeResult getSuperType(EObject obj) {
		ODLOperator operator = EcoreUtil2.getContainerOfType(obj, ODLOperator.class);
		if (operator != null) {
			return new TypeResult(typeUtils.createTypeRef(AbstractPipe.class, typeFactory.getSystemResourceSet()));
		} else {
			return super.getSuperType(obj);
		}
	}
	
}
