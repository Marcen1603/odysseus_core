package de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.xtext.EcoreUtil2;

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.AbstractIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.odl.lookup.ODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;

public class ODLCompilerHelper extends AbstractIQLCompilerHelper<ODLLookUp, ODLTypeFactory> {

	public static final String AO_RULE_OPERATOR = "AORule";
	public static final String AO_OPERATOR = "AO";
	public static final String PO_OPERATOR = "PO";

	@Inject
	public ODLCompilerHelper(ODLLookUp lookUp, ODLTypeFactory factory) {
		super(lookUp, factory);
	}
	
	public Collection<ODLParameter> getParameters(ODLOperator o) {
		return EcoreUtil2.getAllContentsOfType(o, ODLParameter.class);
	}

	public boolean hasValidateMethod(ODLOperator operator, ODLParameter parameter) {
		for (ODLMethod method : EcoreUtil2.getAllContentsOfType(operator, ODLMethod.class)) {
			if (method.isValidate() && method.getSimpleName().equalsIgnoreCase(parameter.getSimpleName())) {
				return true;
			}
		}
		return false;
	}

	public Collection<ODLMethod> getODLMethods(ODLOperator operator) {
		return EcoreUtil2.getAllContentsOfType(operator, ODLMethod.class);
	}

}
