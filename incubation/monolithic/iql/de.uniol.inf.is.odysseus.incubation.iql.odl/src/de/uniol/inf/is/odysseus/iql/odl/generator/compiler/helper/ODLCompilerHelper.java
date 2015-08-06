package de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmFormalParameter;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.AbstractIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.odl.lookup.ODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeUtils;

public class ODLCompilerHelper extends AbstractIQLCompilerHelper<ODLLookUp, ODLTypeFactory, ODLTypeUtils> {

	public static final String AO_RULE_OPERATOR = "AORule";
	public static final String AO_OPERATOR = "AO";
	public static final String PO_OPERATOR = "PO";

	@Inject
	public ODLCompilerHelper(ODLLookUp lookUp, ODLTypeFactory factory, ODLTypeUtils typeUtils) {
		super(lookUp, factory, typeUtils);
	}
	
	public Collection<ODLParameter> getParameters(ODLOperator o) {
		return EcoreUtil2.getAllContentsOfType(o, ODLParameter.class);
	}

	public boolean hasValidateMethod(ODLOperator operator, ODLParameter parameter) {
		for (ODLMethod method : EcoreUtil2.getAllContentsOfType(operator, ODLMethod.class)) {
			if (method.isValidate() && method.getSimpleName() != null && method.getSimpleName().equalsIgnoreCase(parameter.getSimpleName())) {
				return true;
			}
		}
		return false;
	}

	public Collection<ODLMethod> getODLMethods(ODLOperator operator) {
		return EcoreUtil2.getAllContentsOfType(operator, ODLMethod.class);
	}

	public Class<?> determineReadType(ODLOperator operator) {
		for (IQLMethod method : EcoreUtil2.getAllContentsOfType(operator, IQLMethod.class)) {
			if (method.getSimpleName() != null && method.getSimpleName().equals("processNext")) {
				JvmFormalParameter parameter = method.getParameters().get(0);
				if (parameter != null) {
					return typeUtils.getJavaType(typeUtils.getLongName(parameter.getParameterType(), false));
				}
			}
		}
		return Tuple.class;
	}

	public OutputMode determineOutputMode(ODLOperator operator) {
		if (operator.getMetadataList() != null) {
			for (IQLMetadata metadata : operator.getMetadataList().getElements()) {
				if (metadata.equals(ODLTypeFactory.OPERATOR_OUTPUT_MODE)) {
					return OutputMode.MODIFIED_INPUT;
				}
			}
		}
		return OutputMode.MODIFIED_INPUT;
	}
}
