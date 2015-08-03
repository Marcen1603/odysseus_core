package de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionNew;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement;
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.IIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;

public abstract class AbstractIQLCompilerHelper<L extends IIQLLookUp, F extends IIQLTypeFactory,  U extends IIQLTypeUtils> implements IIQLCompilerHelper{

	protected L lookUp;
	
	protected F typeFactory;

	protected U typeUtils;

	
	public AbstractIQLCompilerHelper(L lookUp, F typeFactory, U typeUtils) {
		this.lookUp = lookUp;
		this.typeUtils = typeUtils;
		this.typeFactory = typeFactory;
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
	public Collection<IQLTerminalExpressionNew> getNewExpressions(EObject obj) {
		return EcoreUtil2.getAllContentsOfType(obj, IQLTerminalExpressionNew.class);
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
	public JvmTypeReference getPropertyType(String name, JvmTypeReference typeRef) {
		for (JvmField attr : lookUp.getPublicAttributes(typeRef, false)) {
			if (attr.getSimpleName().equalsIgnoreCase(name)) {
				return attr.getType();
			}
		}
		
		for (JvmOperation meth : lookUp.getPublicMethods(typeRef, false)) {
			if (meth.getSimpleName().equalsIgnoreCase("set"+name)) {
				EList<JvmFormalParameter> parameters = meth.getParameters();
				if (parameters != null && parameters.size() == 1) {
					return parameters.get(0).getParameterType();
				}
			}
		}		
		return null;
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
	public boolean isSetter(String property, JvmTypeReference typeRef, JvmTypeReference parameter) {
		for (JvmOperation op : lookUp.getPublicMethods(typeRef, false)) {
			if (op.getSimpleName().equalsIgnoreCase("set"+property)) {
				for (JvmFormalParameter p : op.getParameters()) {
					String qName = typeUtils.getLongName(p.getParameterType(), true);
					String qName2 = typeUtils.getLongName(parameter, true);
					if (qName.equals(qName2)) {
						return true;
					}
				}
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

}
