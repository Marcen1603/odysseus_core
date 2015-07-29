package de.uniol.inf.is.odysseus.iql.basic.typing.typeoperators;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.typing.IQLDefaultTypes;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.IIQLTypeFactory;

public abstract class AbstractIQLTypeOperatorsFactory<F extends IIQLTypeFactory> implements IIQLTypeOperatorsFactory {
	private Map<String, ITypeOperators> typeOperators = new HashMap<>();
	private Map<ITypeOperators, JvmTypeReference> typeOperatorsRefs = new HashMap<>();

	protected F typeFactory;

	public AbstractIQLTypeOperatorsFactory(F typeFactory) {
		this.typeFactory = typeFactory;
		this.init();
	}
	

	private void init() {
		for (ITypeOperators operators : IQLDefaultTypes.getTypeOperators()) {
			this.addTypeOperators(operators);
		}
	}

	@Override
	public void addTypeOperators(ITypeOperators typeOperators) {
		this.typeOperators.put(typeOperators.getClass().getCanonicalName(), typeOperators);
		JvmTypeReference typeRef = typeFactory.getTypeRef(typeOperators.getClass());
		JvmType innerType = typeFactory.getInnerType(typeRef, false);
		for (JvmOperation method : EcoreUtil2.getAllContentsOfType(innerType, JvmOperation.class)) {
			if (typeOperators.hasExtensionMethod(method.getSimpleName(), method.getParameters().size())) {
				method.getParameters().remove(0);
			}
		}
		this.typeOperatorsRefs.put(typeOperators, typeRef);
	}
	
	@Override
	public void removeTypeOperators(ITypeOperators typeOperators) {
		this.typeOperators.remove(typeOperators.getClass().getCanonicalName());
	}
	
	@Override
	public ITypeOperators getTypeOperators(JvmTypeReference typeRef) {
		return findTypeOperators(typeRef);
	}
	
	
	@Override
	public JvmTypeReference getTypeOperators(ITypeOperators typeOperators) {
		return typeOperatorsRefs.get(typeOperators);
	}

	@Override
	public ITypeOperators getTypeOperators(JvmTypeReference typeRef, String attribute) {
		return findTypeOperators(typeRef, attribute);
	}

	@Override
	public ITypeOperators getTypeOperators(JvmTypeReference typeRef,String method, int args) {
		return findTypeOperators(typeRef, method, args);
	}
	
	@Override
	public boolean hasTypeOperators(JvmTypeReference typeRef, String attribute) {
		return getTypeOperators(typeRef, attribute) != null;
	}

	@Override
	public boolean hasTypeOperators(JvmTypeReference typeRef, String method,int args) {
		return getTypeOperators(typeRef, method, args) != null;
	}
	
	private ITypeOperators findTypeOperators(JvmTypeReference typeRef, String attribute) {
		JvmType innerType = typeFactory.getInnerType(typeRef, true);

		String name = typeFactory.getLongName(innerType, true);
		for (ITypeOperators operators : typeOperators.values()) {
			Class<?> type = operators.getType();
			if (type.getCanonicalName().equalsIgnoreCase(name)) {
				try {
					Field field = type.getDeclaredField(attribute);
					if (field.getName().equalsIgnoreCase(attribute)) {
						return operators;
					}
				} catch (NoSuchFieldException | SecurityException e) {
				}
			}
		}
		if (innerType instanceof JvmGenericType) {
			JvmGenericType genericType = (JvmGenericType) innerType;
			for (JvmTypeReference interf : genericType.getExtendedInterfaces()) {
				ITypeOperators operators = findTypeOperators(interf, attribute);
				if (operators != null) {
					return operators;
				}
			}
			if (genericType.getExtendedClass() != null) {
				return findTypeOperators(genericType.getExtendedClass(), attribute);
			}
		}
		return null;
	}
	
	private ITypeOperators findTypeOperators(JvmTypeReference typeRef, String method, int args) {
		JvmType innerType = typeFactory.getInnerType(typeRef, true);

		String name = typeFactory.getLongName(innerType, true);
		for (ITypeOperators operators : typeOperators.values()) {
			Class<?> type = operators.getType();
			if (type.getCanonicalName().equalsIgnoreCase(name)) {
				try {
					Method m = type.getDeclaredMethod(method);
					if (m.getName().equalsIgnoreCase(method)) {
						return operators;
					}
				} catch (NoSuchMethodException | SecurityException e) {
				} 
			}
		}
		if (innerType instanceof JvmGenericType) {
			JvmGenericType genericType = (JvmGenericType) innerType;
			for (JvmTypeReference interf : genericType.getExtendedInterfaces()) {
				ITypeOperators operators = findTypeOperators(interf, method, args);
				if (operators != null) {
					return operators;
				}
			}
			if (genericType.getExtendedClass() != null) {
				return findTypeOperators(genericType.getExtendedClass(), method, args);
			}
		}
		return null;
	}
	
	private ITypeOperators findTypeOperators(JvmTypeReference typeRef) {
		JvmType innerType = typeFactory.getInnerType(typeRef, true);

		String name = typeFactory.getLongName(innerType, true);
		for (ITypeOperators operators : typeOperators.values()) {
			Class<?> type = operators.getType();
			if (type.getCanonicalName().equalsIgnoreCase(name)) {
				return operators;
			}
		}
		if (innerType instanceof JvmGenericType) {
			JvmGenericType genericType = (JvmGenericType) innerType;
			for (JvmTypeReference interf : genericType.getExtendedInterfaces()) {
				ITypeOperators operators = findTypeOperators(interf);
				if (operators != null) {
					return operators;
				}
			}
			if (genericType.getExtendedClass() != null) {
				return findTypeOperators(genericType.getExtendedClass());
			}
		}
		return null;
	}

	@Override
	public boolean hasTypeOperators(JvmTypeReference typeRef) {
		return getTypeOperators(typeRef)!= null;
	}	

}
