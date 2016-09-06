package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper;

import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper;

public interface IQDLCompilerHelper extends IIQLCompilerHelper{

	boolean isSource(JvmTypeReference typeRef);

	boolean isSourceAttribute(JvmTypeReference typeRef, String name);

	boolean isOperator(JvmTypeReference typeRef);

	boolean isParameter(String parameter, JvmTypeReference opType);

	String getParameterOfGetter(JvmOperation method);

	String getParameterOfSetter(JvmOperation method);

	boolean isParameterGetter(JvmOperation method, JvmTypeReference opType);

	boolean isParameterSetter(JvmOperation method, JvmTypeReference opType);

	String getLogicalOperatorName(JvmTypeReference typeRef);

}
