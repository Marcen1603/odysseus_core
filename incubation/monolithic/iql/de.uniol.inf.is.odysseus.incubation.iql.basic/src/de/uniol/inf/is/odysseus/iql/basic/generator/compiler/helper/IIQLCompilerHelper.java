package de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionNew;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement;

public interface IIQLCompilerHelper {

	String getNodeText(EObject obj);
	
	String getClassName(EObject obj);
	
	Collection<IQLTerminalExpressionNew> getNewExpressions(EObject obj);
	
	Collection<IQLAttribute> getAttributes(EObject obj);
	Collection<IQLVariableStatement> getVarStatements(EObject obj);

	JvmTypeReference getPropertyType(String name, JvmTypeReference typeRef);

	String getMethodName(String name, JvmTypeReference typeRef);

	boolean isPublicAttribute(String name, JvmTypeReference typeRef,
			JvmTypeReference parameter);

	String firstCharUpperCase(String s);

	boolean isSetter(String name, JvmTypeReference typeRef,
			JvmTypeReference parameter);

	Collection<IQLMethod> getMethods(EObject obj);

	IQLClass getClass(IQLStatement stmt);

}
