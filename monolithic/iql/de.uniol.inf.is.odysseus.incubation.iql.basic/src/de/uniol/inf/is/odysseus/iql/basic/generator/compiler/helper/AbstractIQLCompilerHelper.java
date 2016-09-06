package de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmArrayType;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLFactory;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement;
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.builder.IIQLSystemTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.IIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;

public abstract class AbstractIQLCompilerHelper<L extends IIQLLookUp, F extends IIQLTypeDictionary,  U extends IIQLTypeUtils> implements IIQLCompilerHelper{

	protected L lookUp;
	
	protected F typeDictionary;

	protected U typeUtils;

	
	public AbstractIQLCompilerHelper(L lookUp, F typeDictionary, U typeUtils) {
		this.lookUp = lookUp;
		this.typeUtils = typeUtils;
		this.typeDictionary = typeDictionary;
	}
	
	
	@Override
	public String getArrayMethodName(JvmTypeReference typeRef) {
		StringBuilder builder = new StringBuilder();
		builder.append("to");
		JvmType innerType = typeUtils.getInnerType(typeRef, false);
		if (typeUtils.isPrimitive(innerType)) {
			if (typeUtils.isByte(typeUtils.createTypeRef(innerType))) {
				builder.append("Byte");
			} else if(typeUtils.isShort(typeUtils.createTypeRef(innerType))) {
				builder.append("Short");
			} else if(typeUtils.isInt(typeUtils.createTypeRef(innerType))) {
				builder.append("Int");
			} else if(typeUtils.isLong(typeUtils.createTypeRef(innerType))) {
				builder.append("Long");
			} else if(typeUtils.isFloat(typeUtils.createTypeRef(innerType))) {
				builder.append("Float");
			} else if(typeUtils.isDouble(typeUtils.createTypeRef(innerType))) {
				builder.append("Double");
			} else if(typeUtils.isBoolean(typeUtils.createTypeRef(innerType))) {
				builder.append("Boolean");
			}  else if(typeUtils.isCharacter(typeUtils.createTypeRef(innerType))) {
				builder.append("Char");
			}
		}		
		builder.append("Array");
		int dim = typeUtils.getArrayDim(typeRef);
		builder.append(dim);
		return builder.toString();
	}
	
	@Override
	public boolean isPrimitiveArray(JvmTypeReference typeRef) {
		if (typeUtils.isArray(typeRef)) {
			JvmType innerType = typeUtils.getInnerType(typeRef, false);
			return typeUtils.isPrimitive(innerType);
		}
		return false;
	}

	@Override
	public List<String> toList(String element) {
		List<String> result = new ArrayList<>();
		result.add(element);
		return result;
	}
	
	@Override
	public boolean hasSystemTypeCompiler(JvmMember member) {
		return getSystemTypeCompiler(member) != null;
	}

	
	@Override
	public IIQLSystemTypeCompiler getSystemTypeCompiler(JvmMember member) {
		if (member.eContainer() instanceof JvmDeclaredType) {
			JvmDeclaredType type = (JvmDeclaredType) member.eContainer();
			String name = typeUtils.getLongName(type, true);
			return typeDictionary.getSystemTypeCompiler(name);
		} 
		return null;
	}
	
	@Override
	public boolean isJvmArray(JvmTypeReference typeRef) {
		return typeUtils.getInnerType(typeRef, true) instanceof JvmArrayType;
	}
	
	@Override
	public String getNodeText(EObject obj) {
		INode node = NodeModelUtils.getNode(obj);
		if (node != null) {
			return NodeModelUtils.getTokenText(node);
		} else {
			return null;
		}
	}

	@Override
	public String getClassName(EObject obj) {
		IQLClass c = EcoreUtil2.getContainerOfType(obj, IQLClass.class);
		if (c != null) {
			return c.getSimpleName();
		}
		return null;
	}
	
	@Override
	public Collection<IQLMethod> getMethods(EObject obj) {
		return EcoreUtil2.getAllContentsOfType(obj, IQLMethod.class);
	}
	
	@Override
	public Collection<IQLNewExpression> getNewExpressions(EObject obj) {
		return EcoreUtil2.getAllContentsOfType(obj, IQLNewExpression.class);
	}

	@Override
	public Collection<IQLAttribute> getAttributes(EObject obj) {
		return EcoreUtil2.getAllContentsOfType(obj, IQLAttribute.class);
	}

	@Override
	public Collection<IQLVariableStatement> getVarStatements(EObject obj) {
		return EcoreUtil2.getAllContentsOfType(obj, IQLVariableStatement.class);
	}
	
	@Override
	public String getMethodName(String name, JvmTypeReference typeRef) {
		for (JvmOperation meth : lookUp.getPublicMethods(typeRef, false)) {
			if (meth.getSimpleName().equalsIgnoreCase(name)) {
				EList<JvmFormalParameter> parameters = meth.getParameters();
				if (parameters != null && parameters.size() == 1) {
					return meth.getSimpleName();
				}
			}
		}
		return "";
	}
	
	@Override
	public JvmTypeReference getPropertyType(JvmIdentifiableElement jvmElement, JvmTypeReference typeRef) {
		if (jvmElement instanceof JvmField) {
			return ((JvmField) jvmElement).getType();
		} else if (jvmElement instanceof JvmOperation) {
			JvmOperation op = (JvmOperation) jvmElement;
			return op.getParameters().get(0).getParameterType();
		} else {
			return null;
		}
	}
	
	@Override
	public boolean isPublicAttribute(String name, JvmTypeReference typeRef, JvmTypeReference parameter) {
		for (JvmField attr : lookUp.getPublicAttributes(typeRef, false)) {			
			if (attr.getSimpleName().equalsIgnoreCase(name) && typeUtils.getLongName(attr.getType(), true).equals(typeUtils.getLongName(parameter, true))) {
				return true;
			}
		}		
		return false;	
	}

	
	@Override
	public String firstCharUpperCase(String s) {
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}
	
	public boolean isSetter(String name) {		
		if (name.length() > 3) {
			return name.startsWith("set");
		}
		return false;
	}
	
	@Override
	public IQLClass getClass(IQLStatement stmt) {
		IQLClass clazz =  EcoreUtil2.getContainerOfType(stmt, IQLClass.class);
		return clazz;
	}
	
	@Override
	public List<IQLExpression> createGetterArguments(EList<IQLExpression> expressions) {
		List<IQLExpression> result = new ArrayList<>();
		if (expressions.size() == 1) {
			result.add(expressions.get(0));
		} else {
			IQLLiteralExpressionList list = BasicIQLFactory.eINSTANCE.createIQLLiteralExpressionList();
			result.add(list);
		}
		return result;
	}
	
	
	@Override
	public List<IQLExpression> createSetterArguments(IQLExpression expr, EList<IQLExpression> expressions) {
		List<IQLExpression> result = new ArrayList<>();
		result.add(expr);
		if (expressions.size() == 1) {
			result.add(expressions.get(0));
		} else {
			IQLLiteralExpressionList list = BasicIQLFactory.eINSTANCE.createIQLLiteralExpressionList();
			result.add(list);
		}
		return result;
	}

}
