package de.uniol.inf.is.odysseus.iql.basic.scoping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.xtext.ClasspathBasedTypeScope;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;

import com.google.common.base.Predicate;

@SuppressWarnings("restriction")
public class IQLClasspathBasedTypeScope extends ClasspathBasedTypeScope {

	private IScope jdtScope;
	
	private IScope parentScope;

	
	public IQLClasspathBasedTypeScope(IQLClasspathTypeProvider typeProvider,IScope parentScope,IScope jdtScope,
			IQualifiedNameConverter qualifiedNameConverter,
			Predicate<IEObjectDescription> filter) {
		super(typeProvider, qualifiedNameConverter, filter);
		this.parentScope = parentScope;
		this.jdtScope = jdtScope;
	}
	
	@Override
	public IEObjectDescription getSingleElement(QualifiedName name) {
		IEObjectDescription result = jdtScope.getSingleElement(name);
		if (result == null) {
			result = super.getSingleElement(name);
		}
		if (result == null) {
			result = parentScope.getSingleElement(name);
		}
		return result;
	}
	
	
	@Override
	public IEObjectDescription getSingleElement(EObject object) {
		IEObjectDescription result = jdtScope.getSingleElement(object);
		if (result == null) {
			result = super.getSingleElement(object);
		}
		if (result == null) {
			result = parentScope.getSingleElement(object);
		}
		return result;
	}
	
	@Override
	public Iterable<IEObjectDescription> getElements(QualifiedName name) {
		Map<QualifiedName, IEObjectDescription> result = new HashMap<>();
		
		for (IEObjectDescription obj : jdtScope.getElements(name)) {
			result.put(obj.getQualifiedName(), obj);
		}
		
		for (IEObjectDescription obj : super.getElements(name)) {
			if (!result.containsKey(obj.getQualifiedName())) {
				result.put(obj.getQualifiedName(), obj);
			}
		}
		for (IEObjectDescription obj : parentScope.getElements(name)) {
			if (!result.containsKey(obj.getQualifiedName())) {
				result.put(obj.getQualifiedName(), obj);
			}
		}
		return result.values();
	}
	
	@Override
	public Iterable<IEObjectDescription> getElements(EObject object) {
		Map<QualifiedName, IEObjectDescription> result = new HashMap<>();
		
		for (IEObjectDescription obj : jdtScope.getElements(object)) {
			result.put(obj.getQualifiedName(), obj);
		}
		
		for (IEObjectDescription obj : super.getElements(object)) {
			if (!result.containsKey(obj.getQualifiedName())) {
				result.put(obj.getQualifiedName(), obj);
			}
		}
		for (IEObjectDescription obj : parentScope.getElements(object)) {
			if (!result.containsKey(obj.getQualifiedName())) {
				result.put(obj.getQualifiedName(), obj);
			}
		}
		return result.values();
	}
	
	@Override
	public Iterable<IEObjectDescription> getAllElements() {
		Map<QualifiedName, IEObjectDescription> result = new HashMap<>();
		
		for (IEObjectDescription obj : jdtScope.getAllElements()) {
			result.put(obj.getQualifiedName(), obj);
		}
		
		for (IEObjectDescription obj : super.getAllElements()) {
			if (!result.containsKey(obj.getQualifiedName())) {
				result.put(obj.getQualifiedName(), obj);
			}
		}		
		
		for (IEObjectDescription obj : parentScope.getAllElements()) {
			if (!result.containsKey(obj.getQualifiedName())) {
				result.put(obj.getQualifiedName(), obj);
			}
		}
		return result.values();
	}
	
	@Override
	protected Iterable<IEObjectDescription> internalGetAllElements() {
		return new HashSet<>();
	}
	
	@Override
	protected Iterable<IEObjectDescription> getAllLocalElements() {
		return new HashSet<>();
	}
	
	@Override
	public IEObjectDescription getSingleElement(QualifiedName name, boolean binary) {
		try {
			JvmType type = getTypeProvider().findTypeByName(name.toString(), binary);
			if (type == null)
				return null;
			IEObjectDescription result = EObjectDescription.create(name, type);
			return result;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

}
