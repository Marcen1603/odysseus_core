package de.uniol.inf.is.odysseus.iql.basic.typing.exprparser;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAdditiveExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttributeSelection;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBooleanNotExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLEqualityExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInstanceOfExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionBoolean;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionChar;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionDouble;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionInt;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionNull;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionRange;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionString;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalAndExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalOrExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelection;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodSelection;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMultiplicativeExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionNew;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPlusMinusExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPostfixExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPrefixExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLRelationalExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionParenthesis;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionSuper;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionThis;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionVariable;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeCastExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDef;
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.types.Range;
import de.uniol.inf.is.odysseus.iql.basic.typing.TypeResult;
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.context.IIQLExpressionParserContext;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.IIQLTypeFactory;

public abstract class AbstractIQLExpressionParser<T extends IIQLTypeFactory, L extends IIQLLookUp, C extends IIQLExpressionParserContext> implements IIQLExpressionParser {

	
	protected L lookUp;
	protected T typeFactory;
	protected C exprParserContext;

	public AbstractIQLExpressionParser(T typeFactory, L lookUp, C exprParserContext) {
		this.typeFactory = typeFactory;
		this.lookUp = lookUp;
		this.exprParserContext = exprParserContext;
	}
	

	@Inject
	private IQLExpressionParserHelper helper;
	
	@SuppressWarnings("unchecked")
	@Override
	public TypeResult getType(IQLExpression expr) {
		return getType(expr, (C) exprParserContext.cleanCopy());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public TypeResult getType(IQLExpression expr, JvmTypeReference exptectedType) {
		IIQLExpressionParserContext c = exprParserContext.cleanCopy();
		c.setExpectedTypeRef(exptectedType);
		return getType(expr, (C) c);
	}
	
	private TypeResult getType(IQLExpression expr, C context) {
		if (expr instanceof IQLAssignmentExpression) {
			return getType((IQLAssignmentExpression)expr, context);
		} else if (expr instanceof IQLLogicalOrExpression) {
			return getType((IQLLogicalOrExpression)expr, context);
		} else if (expr instanceof IQLLogicalAndExpression) {
			return getType((IQLLogicalAndExpression)expr, context);
		} else if (expr instanceof IQLEqualityExpression) {
			return getType((IQLEqualityExpression)expr, context);
		} else if (expr instanceof IQLRelationalExpression) {
			return getType((IQLRelationalExpression)expr, context);
		} else if (expr instanceof IQLInstanceOfExpression) {
			return getType((IQLInstanceOfExpression)expr, context);
		} else if (expr instanceof IQLAdditiveExpression) {
			return getType((IQLAdditiveExpression)expr, context);
		} else if (expr instanceof IQLMultiplicativeExpression) {
			return getType((IQLMultiplicativeExpression)expr, context);
		} else if (expr instanceof IQLPlusMinusExpression) {
			return getType((IQLPlusMinusExpression)expr, context);
		} else if (expr instanceof IQLBooleanNotExpression) {
			return getType((IQLBooleanNotExpression)expr, context);
		} else if (expr instanceof IQLPrefixExpression) {
			return getType((IQLPrefixExpression)expr, context);
		} else if (expr instanceof IQLTypeCastExpression) {
			return getType((IQLTypeCastExpression)expr, context);
		} else if (expr instanceof IQLPostfixExpression) {
			return getType((IQLPostfixExpression)expr, context);
		} else if (expr instanceof IQLArrayExpression) {
			return getType((IQLArrayExpression)expr, context);
		} else if (expr instanceof IQLMemberSelectionExpression) {
			return getType((IQLMemberSelectionExpression)expr, context);
		} else if (expr instanceof IQLTerminalExpressionVariable) {
			return getType((IQLTerminalExpressionVariable)expr, context);
		} else if (expr instanceof IQLTerminalExpressionThis) {
			return getType((IQLTerminalExpressionThis)expr, context);
		} else if (expr instanceof IQLTerminalExpressionSuper) {
			return getType((IQLTerminalExpressionSuper)expr, context);
		} else if (expr instanceof IQLTerminalExpressionParenthesis) {
			return getType((IQLTerminalExpressionParenthesis)expr, context);
		} else if (expr instanceof IQLTerminalExpressionNew) {
			return getType((IQLTerminalExpressionNew)expr, context);
		} else if (expr instanceof IQLLiteralExpressionDouble) {
			return getType((IQLLiteralExpressionDouble)expr, context);
		} else if (expr instanceof IQLLiteralExpressionInt) {
			return getType((IQLLiteralExpressionInt)expr, context);
		} else if (expr instanceof IQLLiteralExpressionString) {
			return getType((IQLLiteralExpressionString)expr, context);
		} else if (expr instanceof IQLLiteralExpressionBoolean) {
			return getType((IQLLiteralExpressionBoolean)expr, context);
		} else if (expr instanceof IQLLiteralExpressionChar) {
			return getType((IQLLiteralExpressionChar)expr, context);
		} else if (expr instanceof IQLLiteralExpressionRange) {
			return getType((IQLLiteralExpressionRange)expr, context);
		} else if (expr instanceof IQLLiteralExpressionNull) {
			return getType((IQLLiteralExpressionNull)expr, context);
		} else if (expr instanceof IQLLiteralExpressionList) {
			return getType((IQLLiteralExpressionList)expr, context);
		} else if (expr instanceof IQLLiteralExpressionMap) {
			return getType((IQLLiteralExpressionMap)expr, context);
		}
		return new TypeResult();
	}
	
	
	public TypeResult getType(IQLLiteralExpressionDouble expr, C context) {
		JvmTypeReference expectedType = context.getExpectedTypeRef();
		if (expectedType != null) {
			return new TypeResult(expectedType);
		} else {
			return new TypeResult(typeFactory.getTypeRef("double"));
		}
	}
	
	public TypeResult getType(IQLLiteralExpressionInt expr, C context) {
		JvmTypeReference expectedType = context.getExpectedTypeRef();
		if (expectedType != null) {
			return new TypeResult(expectedType);
		} else {
			return new TypeResult(typeFactory.getTypeRef("int"));
		}
	}

	
	public TypeResult getType(IQLLiteralExpressionString expr, C context) {
		return new TypeResult(typeFactory.getTypeRef(String.class));
	}
	
	public TypeResult getType(IQLLiteralExpressionBoolean expr, C context) {
		JvmTypeReference expectedType = context.getExpectedTypeRef();
		if (expectedType != null) {
			return new TypeResult(expectedType);
		} else {
			return new TypeResult(typeFactory.getTypeRef("boolean"));
		}
	}
	
	public TypeResult getType(IQLLiteralExpressionRange expr, C context) {
		return new TypeResult(typeFactory.getTypeRef(Range.class));
	}
	
	public TypeResult getType(IQLLiteralExpressionChar expr, C context) {
		JvmTypeReference expectedType = context.getExpectedTypeRef();
		if (expectedType != null) {
			return new TypeResult(expectedType);
		} else {
			return new TypeResult(typeFactory.getTypeRef("char"));
		}
	}
	
	public TypeResult getType(IQLLiteralExpressionNull expr, C context) {
		return new TypeResult();
	}
	
	public TypeResult getType(IQLLiteralExpressionList expr, C context) {
		return new TypeResult(typeFactory.getTypeRef(List.class));
	}
	
	public TypeResult getType(IQLLiteralExpressionMap expr, C context) {
		return new TypeResult(typeFactory.getTypeRef(Map.class));
	}
	
	public TypeResult getType(IQLTerminalExpressionVariable expr, C context) {
		JvmTypeReference type = expr.getVar().getRef();
		if (type == null) {
			type = expr.getVar().getParameterType();
		}
		return new TypeResult(type);
	}
	
	public TypeResult getType(IQLTerminalExpressionParenthesis expr, C context) {
		return getType(expr.getExpr(), context);
	}
	
	public TypeResult getType(IQLArrayExpression expr, C context) {
		return getType(expr.getLeftOperand(), context);
	}
	
	public TypeResult getType(IQLMemberSelectionExpression expr, C context) {
		IQLMemberSelection sel = expr.getRightOperand();
		if (sel instanceof IQLAttributeSelection) {
			IQLAttributeSelection attr = (IQLAttributeSelection) sel;
			return new TypeResult(((JvmField)attr.getVar()).getType());		
		} else if (sel instanceof IQLMethodSelection) {
			IQLMethodSelection meth = (IQLMethodSelection) sel;
			TypeResult left = getType(expr.getLeftOperand(), context);
			if (!left.isNull()) {
				JvmOperation op = lookUp.findMethod(left.getRef(), meth.getMethod(), meth.getArgs());
				return new TypeResult(op.getReturnType());
			}
			return new TypeResult();
		} else {
			return new TypeResult();
		}
	}

	
	public TypeResult getType(IQLPlusMinusExpression expr, C context) {
		return getType(expr.getOperand(), context);
	}
	
	public TypeResult getType(IQLBooleanNotExpression expr, C context) {
		JvmTypeReference expectedType = context.getExpectedTypeRef();
		if (expectedType != null) {
			return new TypeResult(expectedType);
		} else {
			return new TypeResult(typeFactory.getTypeRef("boolean"));
		}
	}
	
	public TypeResult getType(IQLPrefixExpression expr, C context) {
		return getType(expr.getOperand(), context);
	}
	
	public TypeResult getType(IQLPostfixExpression expr, C context) {
		return getType(expr.getOperand(), context);
	}
	
	public TypeResult getType(IQLTypeCastExpression expr, C context) {
		return new TypeResult(expr.getTargetRef());
	}
	
	public TypeResult getType(IQLTerminalExpressionNew expr, C context) {
		return new TypeResult(expr.getRef());
	}
	
	public TypeResult getType(IQLTerminalExpressionThis expr, C context) {		
		IQLTypeDef c = EcoreUtil2.getContainerOfType(expr, IQLTypeDef.class);
		return new TypeResult(typeFactory.getTypeRef(c));
	}
	
	public TypeResult getType(IQLTerminalExpressionSuper expr, C context) {
		IQLTypeDef c = EcoreUtil2.getContainerOfType(expr, IQLTypeDef.class);
		return new TypeResult(c.getExtendedClass());
	}
	
	public TypeResult getType(IQLRelationalExpression expr, C context) {
		JvmTypeReference expectedType = context.getExpectedTypeRef();
		if (expectedType != null) {
			return new TypeResult(expectedType);
		} else {
			return new TypeResult(typeFactory.getTypeRef("boolean"));
		}
	}
	
	public TypeResult getType(IQLInstanceOfExpression expr, C context) {
		JvmTypeReference expectedType = context.getExpectedTypeRef();
		if (expectedType != null) {
			return new TypeResult(expectedType);
		} else {
			return new TypeResult(typeFactory.getTypeRef("boolean"));
		}
	}
	
	public TypeResult getType(IQLEqualityExpression expr, C context) {
		JvmTypeReference expectedType = context.getExpectedTypeRef();
		if (expectedType != null) {
			return new TypeResult(expectedType);
		} else {
			return new TypeResult(typeFactory.getTypeRef("boolean"));
		}
	}
	
	public TypeResult getType(IQLLogicalAndExpression expr, C context) {
		JvmTypeReference expectedType = context.getExpectedTypeRef();
		if (expectedType != null) {
			return new TypeResult(expectedType);
		} else {
			return new TypeResult(typeFactory.getTypeRef("boolean"));
		}
	}
	
	public TypeResult getType(IQLLogicalOrExpression expr, C context) {
		JvmTypeReference expectedType = context.getExpectedTypeRef();
		if (expectedType != null) {
			return new TypeResult(expectedType);
		} else {
			return new TypeResult(typeFactory.getTypeRef("boolean"));
		}
	}
	
	public TypeResult getType(IQLAssignmentExpression expr, C context) {		
		return getType(expr.getLeftOperand(), context);
	}	
	
	public TypeResult getType(IQLAdditiveExpression expr, C context) {
		JvmTypeReference expectedType = context.getExpectedTypeRef();
		if (expectedType != null) {
			return new TypeResult(expectedType);
		} 
		TypeResult left = getType(expr.getLeftOperand(), context);
		TypeResult right = getType(expr.getRightOperand(), context);
		
		if (!left.isNull() && !right.isNull()) {
			if (expr.getOp().equals("+") && (typeFactory.isString(left.getRef())||typeFactory.isString(right.getRef()))) {
				return new TypeResult(typeFactory.getTypeRef(String.class));
			} else {
				return new TypeResult(helper.determineType(left.getRef(), right.getRef()));
			}
		} else {
			return new TypeResult();
		}
	}
		
	public TypeResult getType(IQLMultiplicativeExpression expr, C context) {
		JvmTypeReference expectedType = context.getExpectedTypeRef();
		if (expectedType != null) {
			return new TypeResult(expectedType);
		} 
		TypeResult left = getType(expr.getLeftOperand(), context);
		TypeResult right = getType(expr.getRightOperand(), context);
		
		if (!left.isNull() && !right.isNull()) {
			return new TypeResult(helper.determineType(left.getRef(), right.getRef()));
		} else {
			return new TypeResult();
		}
		
	}
	
}
