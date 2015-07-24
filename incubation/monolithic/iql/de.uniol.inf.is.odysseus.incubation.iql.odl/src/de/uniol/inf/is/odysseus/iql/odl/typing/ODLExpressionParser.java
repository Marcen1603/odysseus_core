package de.uniol.inf.is.odysseus.iql.odl.typing;

import javax.inject.Inject;

import org.eclipse.xtext.EcoreUtil2;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionSuper;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionThis;
import de.uniol.inf.is.odysseus.iql.basic.typing.TypeResult;
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.AbstractIQLExpressionParser;
import de.uniol.inf.is.odysseus.iql.odl.lookup.ODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.types.impl.useroperator.AbstractODLPO;

public class ODLExpressionParser extends AbstractIQLExpressionParser<ODLTypeFactory, ODLLookUp, ODLExpressionParserContext>{

	@Inject
	public ODLExpressionParser(ODLTypeFactory typeFactory, ODLLookUp lookUp, ODLExpressionParserContext context) {
		super(typeFactory, lookUp, context);
	}
	
	@Override
	public TypeResult getType(IQLTerminalExpressionSuper expr, ODLExpressionParserContext context) {
		ODLOperator operator = EcoreUtil2.getContainerOfType(expr, ODLOperator.class);
		if (operator != null) {
			return new TypeResult(typeFactory.getTypeRef(AbstractPipe.class));
		} else {
			return super.getType(expr, context);
		}
	}
	
	@Override
	public TypeResult getType(IQLTerminalExpressionThis expr, ODLExpressionParserContext context) {
		ODLOperator operator = EcoreUtil2.getContainerOfType(expr, ODLOperator.class);
		if (operator != null) {
			return new TypeResult(typeFactory.getTypeRef(AbstractODLPO.class));
		} else {
			return super.getType(expr, context);
		}
	}

}
