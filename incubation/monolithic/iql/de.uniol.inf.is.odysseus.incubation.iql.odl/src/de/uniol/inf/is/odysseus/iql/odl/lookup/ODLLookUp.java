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
import de.uniol.inf.is.odysseus.iql.odl.typing.factory.IODLTypeFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.typeextension.IODLTypeExtensionsFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;

public class ODLLookUp extends AbstractIQLLookUp<IODLTypeFactory, IODLTypeExtensionsFactory, IODLTypeUtils> implements IODLLookUp{

	@Inject
	public ODLLookUp(IODLTypeFactory typeFactory, IODLTypeExtensionsFactory typeOperatorsFactory,IODLTypeUtils typeUtils) {
		super(typeFactory, typeOperatorsFactory, typeUtils);
	}

	@Override
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
	
	@Override
	public Collection<String> getOperatorMetadataKeys() {
		Collection<String> result = new HashSet<>();
		for(Method method : LogicalOperator.class.getDeclaredMethods()) {
			result.add(method.getName());
		}
		result.add(IODLTypeFactory.OPERATOR_OUTPUT_MODE);
		result.add(IODLTypeFactory.OPERATOR_PERSISTENT);
		return result;
	}
	

	@Override
	public Collection<String> getParameterMetadataKeys() {
		Collection<String> result = new HashSet<>();
		for(Method method : Parameter.class.getDeclaredMethods()) {
			result.add(method.getName());
		}
		return result;
	}

	@Override
	public OutputMode[] getOutputModeValues() {
		return OutputMode.values();
	}
	
	@Override
	public boolean isMap(JvmTypeReference typeRef) {
		return isAssignable(typeUtils.createTypeRef(Map.class, typeFactory.getSystemResourceSet()), typeRef);
	}
	
	@Override
	public boolean isClonable(JvmTypeReference typeRef) {
		return methodFinder.findMethod(getPublicMethods(typeRef, false), "clone", 0, true) != null;
	}
	
	@Override
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
