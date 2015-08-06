package de.uniol.inf.is.odysseus.iql.qdl.ui.hover;

import javax.inject.Inject;

import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement;
import de.uniol.inf.is.odysseus.iql.basic.ui.hover.AbstractIQLEObjectDocumentationProvider;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeUtils;

public class QDLEObjectDocumentationProvider extends AbstractIQLEObjectDocumentationProvider<QDLTypeFactory, QDLTypeUtils> {

	private static String BREAK = "<br>";
	
	@Inject
	public QDLEObjectDocumentationProvider(QDLTypeFactory typeFactory, QDLTypeUtils typeUtils) {
		super(typeFactory, typeUtils);
	}

	@Override
	protected String getDocumentationIQLArgumentsMapKeyValue(IQLArgumentsMapKeyValue keyValue) {		
		JvmTypeReference typeRef = null;
		IQLVariableStatement stmt = EcoreUtil2.getContainerOfType(keyValue, IQLVariableStatement.class);
		IQLNewExpression expr = EcoreUtil2.getContainerOfType(keyValue, IQLNewExpression.class);
		if (stmt != null) {
			typeRef = ((IQLVariableDeclaration)stmt.getVar()).getRef();
		} else {
			typeRef = expr.getRef();
		}
		if (typeFactory.isOperator(typeRef)) {
			Parameter parameter = typeFactory.getOperatorParameterType(typeUtils.getShortName(typeRef, false), keyValue.getKey());
			if (parameter != null) {
				return createParameterDocu(parameter);
			} else {
				return super.getDocumentationIQLArgumentsMapKeyValue(keyValue);
			}
		} else {
			return super.getDocumentationIQLArgumentsMapKeyValue(keyValue);
		}
	}
	
	@Override
	protected String getDocumentationJvmGenericType(JvmGenericType type) {		
		if (typeFactory.isOperator(typeUtils.createTypeRef(type))) {
			IOperatorBuilder b = typeFactory.getOperatorBuilder(type.getSimpleName());
			return createOperatorDocu(b);
		} else {
			return super.getDocumentationJvmGenericType(type);
		}
	}
	
	@Override
	protected String getDocumentationJvmField(JvmField attr) {	
		JvmGenericType genericType = EcoreUtil2.getContainerOfType(attr, JvmGenericType.class);
		if (typeFactory.isOperator(typeUtils.createTypeRef(genericType))) {
			Parameter parameter = typeFactory.getOperatorParameterType(typeUtils.getShortName(genericType, false), attr.getSimpleName());
			if (parameter != null) {
				return createParameterDocu(parameter);
			} else {
				return super.getDocumentationJvmField(attr);
			}
		} else {
			return super.getDocumentationJvmField(attr);
		}
	}
	
	@Override
	protected String getDocumentationJvmOperation(JvmOperation method) {		
		JvmGenericType genericType = EcoreUtil2.getContainerOfType(method, JvmGenericType.class);
		if (typeFactory.isOperator(typeUtils.createTypeRef(genericType))) {
			String parameterName = "";
			if (method.getSimpleName().startsWith("set")) {
				parameterName = method.getSimpleName().substring(3);
			} else if (method.getSimpleName().startsWith("get")) {
				parameterName = method.getSimpleName().substring(3);
			} else if (method.getSimpleName().startsWith("is")) {
				parameterName = method.getSimpleName().substring(2);
			}
			Parameter parameter = typeFactory.getOperatorParameterType(typeUtils.getShortName(genericType, false), parameterName);
			if (parameter != null) {
				return createParameterDocu(parameter);
			} else {
				return super.getDocumentationJvmOperation(method);
			}
		} else {
			return super.getDocumentationJvmOperation(method);
		}
	}
	
	private String createOperatorDocu(IOperatorBuilder builder) {
		StringBuilder b = new StringBuilder();
		b.append("minInputOperatorCount = "+builder.getMinInputOperatorCount()+BREAK);
		b.append("maxInputOperatorCount = "+builder.getMaxInputOperatorCount()+BREAK);
		b.append("categories = ");
		int i = 0; 
		for (String category : builder.getCategories()) {
			if (i>0) {
				b.append(", ");
			}
			i++;
			b.append(category);
		}
		b.append(BREAK);		
		b.append("doc = "+builder.getDoc()+BREAK);
		b.append("url = <a href=\"url\">"+builder.getUrl()+"</a>"+BREAK);
		return b.toString();
	}
	
	private String createParameterDocu(Parameter parameter) {
		StringBuilder b = new StringBuilder();
		b.append("type = "+parameter.type().getSimpleName()+BREAK);
		b.append("optional = " +parameter.optional()+BREAK);
		b.append("deprecated = " +parameter.deprecated()+BREAK);
		b.append("isList = " +parameter.isList()+BREAK);
		b.append("isMap = " +parameter.isMap()+BREAK);
		if (parameter.isMap()) {
			b.append("keytype = " +parameter.keytype()+BREAK);
		}
		b.append("doc = " +parameter.doc()+BREAK);
		return b.toString();
	}
}
