package de.uniol.inf.is.odysseus.iql.qdl.ui.hover;

import javax.inject.Inject;

import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement;
import de.uniol.inf.is.odysseus.iql.basic.ui.hover.AbstractIQLEObjectDocumentationProvider;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.IQDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;

public class QDLEObjectDocumentationProvider extends AbstractIQLEObjectDocumentationProvider<IQDLTypeDictionary, IQDLTypeUtils> {

	private static String BREAK = "<br>";
	
	@Inject
	private IQDLLookUp lookUp;
	
	@Inject
	public QDLEObjectDocumentationProvider(IQDLTypeDictionary typeDictionary, IQDLTypeUtils typeUtils) {
		super(typeDictionary, typeUtils);
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
		if (lookUp.isOperator(typeRef)) {
			Parameter parameter = typeDictionary.getOperatorParameterType(typeUtils.getShortName(typeRef, false), keyValue.getKey().getSimpleName());
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
	protected String getDocumentationJvmDeclaredType(JvmDeclaredType type) {		
		if (lookUp.isOperator(typeUtils.createTypeRef(type))) {
			IOperatorBuilder b = typeDictionary.getOperatorBuilder(type.getSimpleName());
			return createOperatorDocu(b);
		} else {
			return super.getDocumentationJvmDeclaredType(type);
		}
	}
	
	@Override
	protected String getDocumentationJvmField(JvmField attr) {	
		JvmDeclaredType declaredType = EcoreUtil2.getContainerOfType(attr, JvmDeclaredType.class);
		if (lookUp.isOperator(typeUtils.createTypeRef(declaredType))) {
			Parameter parameter = typeDictionary.getOperatorParameterType(typeUtils.getShortName(declaredType, false), attr.getSimpleName());
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
		JvmDeclaredType declaredType = EcoreUtil2.getContainerOfType(method, JvmDeclaredType.class);
		if (lookUp.isOperator(typeUtils.createTypeRef(declaredType))) {
			String parameterName = "";
			if (method.getSimpleName().startsWith("set")) {
				parameterName = method.getSimpleName().substring(3);
			} else if (method.getSimpleName().startsWith("get")) {
				parameterName = method.getSimpleName().substring(3);
			} else if (method.getSimpleName().startsWith("is")) {
				parameterName = method.getSimpleName().substring(2);
			}
			Parameter parameter = typeDictionary.getOperatorParameterType(typeUtils.getShortName(declaredType, false), parameterName);
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
