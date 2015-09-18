package de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper;

import java.util.Collection;

import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter;

public interface IODLCompilerHelper extends IIQLCompilerHelper {

	public static final String AO_RULE_OPERATOR = "AORule";
	public static final String AO_OPERATOR = "AO";
	public static final String PO_OPERATOR = "PO";
	
	Collection<ODLParameter> getParameters(ODLOperator operator);

	boolean hasValidateMethod(ODLOperator operator, ODLParameter parameter);

	Collection<ODLMethod> getODLMethods(ODLOperator operator);

	JvmTypeReference determineReadType(ODLOperator operator);

	OutputMode determineOutputMode(ODLOperator operator);

	boolean hasPredicate(ODLOperator operator);

	Collection<String> getPredicates(ODLOperator operator);

	Collection<String> getPredicateArrays(ODLOperator operator);

	boolean hasPOInitMethod(ODLOperator operator);

	boolean hasOperatorValidate(ODLOperator operator);

	boolean hasProcessNext(ODLOperator operator);

	boolean hasProcessPunctuation(ODLOperator operator);

	JvmTypeReference determineMetadataType(ODLOperator operator);

	public Collection<IQLAttribute> getAOAttributes(ODLOperator operator);

	public Collection<IQLMethod> getAOMethods(ODLOperator operator);

	public Collection<IQLAttribute> getPOAttributes(ODLOperator operator);

	public Collection<JvmMember> getPOMembers(ODLOperator operator);

	public Collection<IQLAttribute> getAOAndPOAttributes(ODLOperator operator);

	Collection<IQLMethod> getPOMethods(ODLOperator operator);

	public Collection<JvmMember> getAOMembers(ODLOperator operator);

}
