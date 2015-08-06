package de.uniol.inf.is.odysseus.iql.basic.typing.exprparser;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAdditiveExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBooleanNotExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLEqualityExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInstanceOfExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJvmElementCallExpression;
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
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMultiplicativeExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLParenthesisExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPlusMinusExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPostfixExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPrefixExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLRelationalExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSuperExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLThisExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeCastExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.types.Range;
import de.uniol.inf.is.odysseus.iql.basic.typing.TypeResult;
import de.uniol.inf.is.odysseus.iql.basic.typing.exprparser.context.IIQLExpressionParserContext;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.IIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;

public abstract class AbstractIQLExpressionParser<T extends IIQLTypeFactory, L extends IIQLLookUp, C extends IIQLExpressionParserContext, U extends IIQLTypeUtils> implements IIQLExpressionParser {

	
	protected L lookUp;
	protected T typeFactory;
	protected C exprParserContext;
	protected U typeUtils;

	public AbstractIQLExpressionParser(T typeFactory, L lookUp, C exprParserContext, U typeUtils) {
		this.typeFactory = typeFactory;
		this.lookUp = lookUp;
		this.exprParserContext = exprParserContext;
		this.typeUtils = typeUtils;
	}
	
	
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
		} else if (expr instanceof IQLJvmElementCallExpression) {
			return getType((IQLJvmElementCallExpression)expr, context);
		} else if (expr instanceof IQLThisExpression) {
			return getType((IQLThisExpression)expr, context);
		} else if (expr instanceof IQLSuperExpression) {
			return getType((IQLSuperExpression)expr, context);
		} else if (expr instanceof IQLParenthesisExpression) {
			return getType((IQLParenthesisExpression)expr, context);
		} else if (expr instanceof IQLNewExpression) {
			return getType((IQLNewExpression)expr, context);
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
			return new TypeResult(typeUtils.createTypeRef("double", typeFactory.getSystemResourceSet()));
		}
	}
	
	public TypeResult getType(IQLLiteralExpressionInt expr, C context) {
		JvmTypeReference expectedType = context.getExpectedTypeRef();
		if (expectedType != null) {
			return new TypeResult(expectedType);
		} else {
			return new TypeResult(typeUtils.createTypeRef("int", typeFactory.getSystemResourceSet()));
		}
	}

	
	public TypeResult getType(IQLLiteralExpressionString expr, C context) {
		return new TypeResult(typeUtils.createTypeRef(String.class, typeFactory.getSystemResourceSet()));
	}
	
	public TypeResult getType(IQLLiteralExpressionBoolean expr, C context) {
		JvmTypeReference expectedType = context.getExpectedTypeRef();
		if (expectedType != null) {
			return new TypeResult(expectedType);
		} else {
			return new TypeResult(typeUtils.createTypeRef("boolean", typeFactory.getSystemResourceSet()));
		}
	}
	
	public TypeResult getType(IQLLiteralExpressionRange expr, C context) {
		return new TypeResult(typeUtils.createTypeRef(Range.class, typeFactory.getSystemResourceSet()));
	}
	
	public TypeResult getType(IQLLiteralExpressionChar expr, C context) {
		JvmTypeReference expectedType = context.getExpectedTypeRef();
		if (expectedType != null) {
			return new TypeResult(expectedType);
		} else {
			return new TypeResult(typeUtils.createTypeRef("char", typeFactory.getSystemResourceSet()));
		}
	}
	
	public TypeResult getType(IQLLiteralExpressionNull expr, C context) {
		return new TypeResult();
	}
	
	public TypeResult getType(IQLLiteralExpressionList expr, C context) {
		return new TypeResult(typeUtils.createTypeRef(List.class, typeFactory.getSystemResourceSet()));
	}
	
	public TypeResult getType(IQLLiteralExpressionMap expr, C context) {
		return new TypeResult(typeUtils.createTypeRef(Map.class, typeFactory.getSystemResourceSet()));
	}
	
	public TypeResult getType(IQLJvmElementCallExpression expr, C context) {
		JvmTypeReference type = null;
		if (expr.getElement() instanceof IQLVariableDeclaration) {
			type = ((IQLVariableDeclaration) expr.getElement()).getRef();
		} else if (expr.getElement() instanceof JvmField) {
			type = ((JvmField) expr.getElement()).getType();
		} else if (expr.getElement() instanceof JvmFormalParameter) {
			type = ((JvmFormalParameter) expr.getElement()).getParameterType();
		} else if (expr.getElement() instanceof JvmOperation) {
			type = ((JvmOperation) expr.getElement()).getReturnType();
		}
		return new TypeResult(type);
	}

	
	public TypeResult getType(IQLParenthesisExpression expr, C context) {
		return getType(expr.getExpr(), context);
	}
	
	public TypeResult getType(IQLArrayExpression expr, C context) {
		return getType(expr.getLeftOperand(), context);
	}
	
	public TypeResult getType(IQLMemberSelectionExpression expr, C context) {
		if (expr.getSel().getMember() instanceof JvmField) {
			return new TypeResult(((JvmField)expr.getSel().getMember()).getType());		
		} else if (expr.getSel().getMember() instanceof JvmOperation) {
			return new TypeResult(((JvmOperation)expr.getSel().getMember()).getReturnType());		
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
			return new TypeResult(typeUtils.createTypeRef("boolean", typeFactory.getSystemResourceSet()));
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
	
	public TypeResult getType(IQLNewExpression expr, C context) {
		return new TypeResult(expr.getRef());
	}
	
	public TypeResult getType(IQLThisExpression expr, C context) {		
		JvmGenericType c = EcoreUtil2.getContainerOfType(expr, JvmGenericType.class);
		return new TypeResult(typeUtils.createTypeRef(c));
	}
	
	public TypeResult getType(IQLSuperExpression expr, C context) {
		JvmGenericType c = EcoreUtil2.getContainerOfType(expr, JvmGenericType.class);
		return new TypeResult(c.getExtendedClass());
	}
	
	@Override
	public TypeResult getThisType(EObject obj) {
		JvmGenericType c = EcoreUtil2.getContainerOfType(obj, JvmGenericType.class);
		return new TypeResult(typeUtils.createTypeRef(c));
	}

	@Override
	public TypeResult getSuperType(EObject obj) {
		JvmGenericType c = EcoreUtil2.getContainerOfType(obj, JvmGenericType.class);
		return new TypeResult(c.getExtendedClass());
	}
	
	@Override
	public boolean isThis(EObject obj) {
		return obj instanceof IQLThisExpression;
	}

	@Override
	public boolean isSuper(EObject obj) {
		return obj instanceof IQLSuperExpression;
	}
	
	public TypeResult getType(IQLRelationalExpression expr, C context) {
		JvmTypeReference expectedType = context.getExpectedTypeRef();
		if (expectedType != null) {
			return new TypeResult(expectedType);
		} else {
			return new TypeResult(typeUtils.createTypeRef("boolean", typeFactory.getSystemResourceSet()));
		}
	}
	
	public TypeResult getType(IQLInstanceOfExpression expr, C context) {
		JvmTypeReference expectedType = context.getExpectedTypeRef();
		if (expectedType != null) {
			return new TypeResult(expectedType);
		} else {
			return new TypeResult(typeUtils.createTypeRef("boolean", typeFactory.getSystemResourceSet()));
		}
	}
	
	public TypeResult getType(IQLEqualityExpression expr, C context) {
		JvmTypeReference expectedType = context.getExpectedTypeRef();
		if (expectedType != null) {
			return new TypeResult(expectedType);
		} else {
			return new TypeResult(typeUtils.createTypeRef("boolean", typeFactory.getSystemResourceSet()));
		}
	}
	
	public TypeResult getType(IQLLogicalAndExpression expr, C context) {
		JvmTypeReference expectedType = context.getExpectedTypeRef();
		if (expectedType != null) {
			return new TypeResult(expectedType);
		} else {
			return new TypeResult(typeUtils.createTypeRef("boolean", typeFactory.getSystemResourceSet()));
		}
	}
	
	public TypeResult getType(IQLLogicalOrExpression expr, C context) {
		JvmTypeReference expectedType = context.getExpectedTypeRef();
		if (expectedType != null) {
			return new TypeResult(expectedType);
		} else {
			return new TypeResult(typeUtils.createTypeRef("boolean", typeFactory.getSystemResourceSet()));
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
			if (expr.getOp().equals("+") && (typeUtils.isString(left.getRef())||typeUtils.isString(right.getRef()))) {
				return new TypeResult(typeUtils.createTypeRef(String.class, typeFactory.getSystemResourceSet()));
			} else {
				return new TypeResult(determineType(left.getRef(), right.getRef()));
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
			return new TypeResult(determineType(left.getRef(), right.getRef()));
		} else {
			return new TypeResult();
		}
		
	}
	
	protected JvmTypeReference determineType(JvmTypeReference left, JvmTypeReference right) {
		if (typeUtils.isDouble(left) || typeUtils.isDouble(right)) {
			return typeUtils.createTypeRef("double", typeFactory.getSystemResourceSet());
		} else if (typeUtils.isFloat(left) || typeUtils.isFloat(right)) {
			return typeUtils.createTypeRef("float", typeFactory.getSystemResourceSet());
		} else if (typeUtils.isLong(left) || typeUtils.isLong(right)) {
			return typeUtils.createTypeRef("long", typeFactory.getSystemResourceSet());
		} else {
			return typeUtils.createTypeRef("int", typeFactory.getSystemResourceSet());		
		}
	}
	
}
