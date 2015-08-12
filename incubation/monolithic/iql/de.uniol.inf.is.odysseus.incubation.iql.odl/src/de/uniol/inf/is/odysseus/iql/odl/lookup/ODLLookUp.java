package de.uniol.inf.is.odysseus.iql.odl.lookup;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


import org.eclipse.xtext.common.types.JvmArrayType;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayType;
import de.uniol.inf.is.odysseus.iql.basic.lookup.AbstractIQLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeExtensionsFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeUtils;

public class ODLLookUp extends AbstractIQLLookUp<ODLTypeFactory, ODLTypeExtensionsFactory, ODLTypeUtils>{

	@Inject
	public ODLLookUp(ODLTypeFactory typeFactory, ODLTypeExtensionsFactory typeOperatorsFactory,ODLTypeUtils typeUtils) {
		super(typeFactory, typeOperatorsFactory, typeUtils);
	}

	public Collection<JvmOperation> getOnMethods() {
		Collection<JvmOperation> result = new HashSet<>();

		JvmTypeReference poTypeRef = typeUtils.createTypeRef(AbstractPipe.class, typeFactory.getSystemResourceSet());
		for (JvmOperation op : super.getProtectedMethods(poTypeRef, false)) {
			if (op.getSimpleName().startsWith("on")) {
				result.add(op);
			}
		}
		
		JvmTypeReference aoTypeRef = typeUtils.createTypeRef(AbstractLogicalOperator.class, typeFactory.getSystemResourceSet());
		for (JvmOperation op : super.getProtectedMethods(aoTypeRef, false)) {
			if (op.getSimpleName().startsWith("on")) {
				result.add(op);
			}
		}
		return result;
	}

	public Collection<String> getOperatorMetadataKeys() {
		Collection<String> result = new HashSet<>();
		for(Method method : LogicalOperator.class.getDeclaredMethods()) {
			result.add(method.getName());
		}
		result.add(ODLTypeFactory.OPERATOR_OUTPUT_MODE);
		result.add(ODLTypeFactory.OPERATOR_PERSISTENT);
		return result;
	}
	

	public Collection<String> getParameterMetadataKeys() {
		Collection<String> result = new HashSet<>();
		for(Method method : Parameter.class.getDeclaredMethods()) {
			result.add(method.getName());
		}
		return result;
	}

	public OutputMode[] getOutputModeValues() {
		return OutputMode.values();
	}
	
	public boolean isMap(JvmTypeReference typeRef) {
		return isAssignable(typeUtils.createTypeRef(Map.class, typeFactory.getSystemResourceSet()), typeRef);
	}
	
	public boolean isClonable(JvmTypeReference typeRef) {
		return methodFinder.findMethod(getPublicMethods(typeRef, false), "clone", 0, true) != null;
	}
	
	public boolean isList(JvmTypeReference typeRef) {
		if (typeUtils.getInnerType(typeRef, true) instanceof IQLArrayType) {
			return true;
		} else if (typeUtils.getInnerType(typeRef, true) instanceof JvmArrayType) {
			return true;
		} else {
			return isAssignable(typeUtils.createTypeRef(List.class, typeFactory.getSystemResourceSet()), typeRef);
		}
	}

}