package de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper;

import java.util.Collection;
import java.util.HashSet;

import javax.inject.Inject;

import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleString;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.AbstractIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.odl.lookup.IODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter;
import de.uniol.inf.is.odysseus.iql.odl.typing.dictionary.IODLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;

public class ODLCompilerHelper extends AbstractIQLCompilerHelper<IODLLookUp, IODLTypeDictionary, IODLTypeUtils> implements IODLCompilerHelper {

	@Inject
	private IQLQualifiedNameConverter converter;
	
	@Inject
	public ODLCompilerHelper(IODLLookUp lookUp, IODLTypeDictionary typeDictionary, IODLTypeUtils typeUtils) {
		super(lookUp, typeDictionary, typeUtils);
	}
	
	@Override
	public Collection<ODLParameter> getParameters(ODLOperator operator) {
		return EcoreUtil2.getAllContentsOfType(operator, ODLParameter.class);
	}
	
	@Override
	public boolean hasValidateMethod(ODLOperator operator, ODLParameter parameter) {
		for (ODLMethod method : EcoreUtil2.getAllContentsOfType(operator, ODLMethod.class)) {
			if (method.isValidate() && method.getSimpleName() != null && method.getSimpleName().equalsIgnoreCase(parameter.getSimpleName())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Collection<ODLMethod> getODLMethods(ODLOperator operator) {
		return EcoreUtil2.getAllContentsOfType(operator, ODLMethod.class);
	}

	@Override
	public JvmTypeReference determineReadType(ODLOperator operator) {
		for (IQLMethod method : EcoreUtil2.getAllContentsOfType(operator, IQLMethod.class)) {
			if (method.getSimpleName() != null && method.getSimpleName().equals("processNext")) {
				JvmFormalParameter parameter = method.getParameters().get(0);
				return parameter.getParameterType();

			}
		}
		return typeUtils.createTypeRef(Tuple.class, typeDictionary.getSystemResourceSet());
	}

	@Override
	public OutputMode determineOutputMode(ODLOperator operator) {
		if (operator.getMetadataList() != null) {
			for (IQLMetadata metadata : operator.getMetadataList().getElements()) {
				if (metadata.getValue() instanceof IQLMetadataValueSingleString) {
					String value = ((IQLMetadataValueSingleString)metadata.getValue()).getValue();					
					try {
						return OutputMode.valueOf(value);
					}catch (Exception e) {
						
					}
				}				
			}
		}
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	public boolean hasPredicate(ODLOperator operator) {
		return !getPredicates(operator).isEmpty() || !getPredicateArrays(operator).isEmpty();
	}

	@Override
	public Collection<String> getPredicates(ODLOperator operator) {
		Collection<String> predicates = new HashSet<>();
		for (ODLParameter parameter : EcoreUtil2.getAllContentsOfType(operator, ODLParameter.class)) {
			if (converter.toJavaString(typeUtils.getLongName(parameter.getType(), true)).equals(IPredicate.class.getCanonicalName())) {
				predicates.add(parameter.getSimpleName());
			}
		}
		return predicates;
	}
	
	@Override
	public Collection<String> getPredicateArrays(ODLOperator operator) {
		Collection<String> predicates = new HashSet<>();
		for (ODLParameter parameter : EcoreUtil2.getAllContentsOfType(operator, ODLParameter.class)) {
			if (typeUtils.isArray(parameter.getType())) {
				if (converter.toJavaString(typeUtils.getLongName(parameter.getType(), false)).equals(IPredicate.class.getCanonicalName())) {
					predicates.add(parameter.getSimpleName());
				}

			}
		}
		return predicates;
	}

	@Override
	public boolean hasInitMethod(ODLOperator operator) {
		for (IQLMethod method : EcoreUtil2.getAllContentsOfType(operator, IQLMethod.class)) {
			if (method.getSimpleName() != null && method.getSimpleName().equals("init") && method.getParameters().size() == 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasOperatorValidate(ODLOperator operator) {
		for (ODLMethod method : EcoreUtil2.getAllContentsOfType(operator, ODLMethod.class)) {
			if (method.isValidate() &&  method.getSimpleName() == null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasProcessNext(ODLOperator operator) {
		for (IQLMethod method : EcoreUtil2.getAllContentsOfType(operator, IQLMethod.class)) {
			if (method.getSimpleName() != null && method.getSimpleName().equals("process_next") && method.getParameters().size() == 2) {
				return true;
			} else if (method.getSimpleName() != null && method.getSimpleName().equals("processNext") && method.getParameters().size() == 2) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasProcessPunctuation(ODLOperator operator) {
		for (IQLMethod method : EcoreUtil2.getAllContentsOfType(operator, IQLMethod.class)) {
			if (method.getSimpleName() != null && method.getSimpleName().equals("processPunctuation") && method.getParameters().size() == 2) {
				return true;
			}
		}
		return false;
	}

	@Override
	public JvmTypeReference determineMetadataType(ODLOperator operator) {
		return typeUtils.createTypeRef(IMetaAttribute.class, typeDictionary.getSystemResourceSet());
	}
}
