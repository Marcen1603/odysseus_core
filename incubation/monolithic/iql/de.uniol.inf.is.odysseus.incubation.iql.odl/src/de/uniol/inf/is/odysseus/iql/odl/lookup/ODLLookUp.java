package de.uniol.inf.is.odysseus.iql.odl.lookup;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;

import javax.inject.Inject;











import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.iql.basic.lookup.AbstractIQLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.typing.dictionary.IODLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.odl.typing.typeextension.IODLTypeExtensionsDictionary;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;

public class ODLLookUp extends AbstractIQLLookUp<IODLTypeDictionary, IODLTypeExtensionsDictionary, IODLTypeUtils> implements IODLLookUp{

	@Inject
	public ODLLookUp(IODLTypeDictionary typeDictionary, IODLTypeExtensionsDictionary typeExtensionsDictionary,IODLTypeUtils typeUtils) {
		super(typeDictionary, typeExtensionsDictionary, typeUtils);
	}
	
	@Override
	public boolean isAssignable(JvmTypeReference targetRef, JvmTypeReference typeRef) {
		if (typeUtils.getInnerType(targetRef, true) instanceof ODLOperator) {
			return super.isAssignable(typeUtils.createTypeRef(AbstractPipe.class, typeDictionary.getSystemResourceSet()), typeRef);
		} else if (typeUtils.getInnerType(typeRef, true) instanceof ODLOperator) {
			return super.isAssignable(targetRef, typeUtils.createTypeRef(AbstractPipe.class, typeDictionary.getSystemResourceSet()));
		} else {
			return super.isAssignable(targetRef, typeRef);
		}
	}
	
	@Override
	public JvmTypeReference getSuperType(EObject obj) {
		ODLOperator operator = EcoreUtil2.getContainerOfType(obj, ODLOperator.class);
		if (operator != null) {
			return typeUtils.createTypeRef(AbstractPipe.class, typeDictionary.getSystemResourceSet());
		} else {
			return super.getSuperType(obj);
		}
	}

	@Override
	public Collection<String> getOperatorMetadataKeys() {
		Collection<String> result = new HashSet<>();
		for(Method method : LogicalOperator.class.getDeclaredMethods()) {
			result.add(method.getName());
		}
		result.add(IODLTypeDictionary.OPERATOR_OUTPUT_MODE);
		result.add(IODLTypeDictionary.OPERATOR_PERSISTENT);
		return result;
	}
	
	@Override
	public Collection<String> getOperatorMetadataValues(String key) {
		Collection<String> result = new HashSet<>();
		if (key.equalsIgnoreCase(IODLTypeDictionary.OPERATOR_OUTPUT_MODE)) {
			for (OutputMode mode : getOutputModeValues()) {
				result.add("\""+mode.toString()+"\"");
			}
		} else if (key.equalsIgnoreCase(IODLTypeDictionary.OPERATOR_PERSISTENT)) {
			result.add("true");
			result.add("false");
		}
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
	public Collection<String> getParameterMetadataValues(String key) {
		Collection<String> result = new HashSet<>();		
		return result;
	}

	@Override
	public OutputMode[] getOutputModeValues() {
		return OutputMode.values();
	}
	
	@Override
	public boolean isClonable(JvmTypeReference typeRef) {
		return methodFinder.findMethod(getPublicMethods(typeRef, false), "clone", 0, true) != null;
	}
	
}
