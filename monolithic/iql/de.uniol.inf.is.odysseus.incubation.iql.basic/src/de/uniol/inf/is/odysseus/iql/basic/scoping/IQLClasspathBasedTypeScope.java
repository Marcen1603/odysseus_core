package de.uniol.inf.is.odysseus.iql.basic.scoping;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;














import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.xtext.ClasspathBasedTypeScope;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;
import org.eclipse.xtext.scoping.IScope;

import com.google.common.base.Predicate;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;


@SuppressWarnings("restriction")
public class IQLClasspathBasedTypeScope extends ClasspathBasedTypeScope {

	private IScope jdtScope;
	
	private IScope parentScope;
	
	private ResourceDescriptionsProvider resourceDescriptionsProvider;
	
	private ResourceSet resourceSet;
	
	private Resource resource;

	private IIQLCrossReferenceValidator validator;

	public IQLClasspathBasedTypeScope(IQLClasspathTypeProvider typeProvider,IScope parentScope,IScope jdtScope,
			IQualifiedNameConverter qualifiedNameConverter, ResourceDescriptionsProvider resourceDescriptionsProvider,
			IIQLCrossReferenceValidator validator, Resource resource,Predicate<IEObjectDescription> filter) {
		super(typeProvider, qualifiedNameConverter, filter);
		this.parentScope = parentScope;
		this.jdtScope = jdtScope;
		this.resourceDescriptionsProvider = resourceDescriptionsProvider;
		this.resourceSet = EcoreUtil2.getResourceSet(resource);
		this.resource = resource;
		this.validator = validator;
	}
	
	private IEObjectDescription findInIndex(QualifiedName qualifiedName) {
		IResourceDescriptions descriptions = resourceDescriptionsProvider.getResourceDescriptions(resourceSet);
		Iterable<IEObjectDescription> exportedClasses = descriptions.getExportedObjects(BasicIQLPackage.Literals.IQL_CLASS, qualifiedName, false);
		Iterable<IEObjectDescription> exportedInterfaces = descriptions.getExportedObjects(BasicIQLPackage.Literals.IQL_INTERFACE, qualifiedName, false);
		for (IEObjectDescription desc : exportedClasses) {
			if (validator.isValidCrossReference(resource, desc)) {
				return desc;
			}
		}
		for (IEObjectDescription desc : exportedInterfaces) {
			if (validator.isValidCrossReference(resource, desc)) {
				return desc;
			}
		}
		return null;
	}
	
	private IEObjectDescription findInIndex(EObject object) {
		IResourceDescriptions descriptions = resourceDescriptionsProvider.getResourceDescriptions(resourceSet);
		Iterable<IEObjectDescription> exportedElements = descriptions.getExportedObjectsByObject(object);
		for (IEObjectDescription desc : exportedElements) {
			if (validator.isValidCrossReference(resource, desc)) {
				return desc;
			}
		}		 
		return null;
	}
	
	private Collection<IEObjectDescription> getElementsFromIndex(QualifiedName qualifiedName) {
		Collection<IEObjectDescription> result = new HashSet<>();
		IResourceDescriptions descriptions = resourceDescriptionsProvider.getResourceDescriptions(resourceSet);
		Iterable<IEObjectDescription> exportedClasses = descriptions.getExportedObjects(BasicIQLPackage.Literals.IQL_CLASS, qualifiedName, false);
		Iterable<IEObjectDescription> exportedInterfaces = descriptions.getExportedObjects(BasicIQLPackage.Literals.IQL_INTERFACE, qualifiedName, false);
		for (IEObjectDescription desc : exportedClasses) {
			if (validator.isValidCrossReference(resource, desc)) {
				result.add(desc);
			}
		}
		for (IEObjectDescription desc : exportedInterfaces) {
			if (validator.isValidCrossReference(resource, desc)) {
				result.add(desc);
			}
		}	
		return result;
	}
	
	private Collection<IEObjectDescription> getElementsFromIndex(EObject object) {
		Collection<IEObjectDescription> result = new HashSet<>();
		IResourceDescriptions descriptions = resourceDescriptionsProvider.getResourceDescriptions(resourceSet);
		Iterable<IEObjectDescription> exportedElements = descriptions.getExportedObjectsByObject(object);
		for (IEObjectDescription desc : exportedElements) {
			if (validator.isValidCrossReference(resource, desc)) {
				result.add(desc);
			}
		}	
		return result;
	}
	
	private Collection<IEObjectDescription> getElementsFromIndex() {
		Collection<IEObjectDescription> result = new HashSet<>();
		IResourceDescriptions descriptions = resourceDescriptionsProvider.getResourceDescriptions(resourceSet);
		Iterable<IEObjectDescription> exportedElements = descriptions.getExportedObjects();
		for (IEObjectDescription desc : exportedElements) {
			if (validator.isValidCrossReference(resource, desc)) {
				result.add(desc);
			}
		}	
		return result;
	}
	
	@Override
	public IEObjectDescription getSingleElement(QualifiedName name) {
		IEObjectDescription result = findInIndex(name);
		
		if (result == null) {
			result = jdtScope.getSingleElement(name);
		}
		
		if (result == null) {
			result = parentScope.getSingleElement(name);
		}
		
		if (result == null) {
			result = super.getSingleElement(name);
		}
		return result;
	}
	
	
	@Override
	public IEObjectDescription getSingleElement(EObject object) {
		IEObjectDescription result = findInIndex(object);

		if (result == null) {
			result = jdtScope.getSingleElement(object);
		}
		
		if (result == null) {
			result = parentScope.getSingleElement(object);
		}
		if (result == null) {
			result = super.getSingleElement(object);
		}
		return result;
	}
	
	@Override
	public Iterable<IEObjectDescription> getElements(QualifiedName name) {
		Map<QualifiedName, IEObjectDescription> result = new HashMap<>();
		
		for (IEObjectDescription obj : getElementsFromIndex(name)) {
			result.put(obj.getQualifiedName(), obj);
		}
		
		for (IEObjectDescription obj : jdtScope.getElements(name)) {
			if (!result.containsKey(obj.getQualifiedName())) {
				result.put(obj.getQualifiedName(), obj);
			}
		}
		
		for (IEObjectDescription obj : parentScope.getElements(name)) {
			if (!result.containsKey(obj.getQualifiedName())) {
				result.put(obj.getQualifiedName(), obj);
			}
		}
		
		for (IEObjectDescription obj : super.getElements(name)) {
			if (!result.containsKey(obj.getQualifiedName())) {
				result.put(obj.getQualifiedName(), obj);
			}
		}
		return result.values();
	}
	
	@Override
	public Iterable<IEObjectDescription> getElements(EObject object) {
		Map<QualifiedName, IEObjectDescription> result = new HashMap<>();
		
		for (IEObjectDescription obj : getElementsFromIndex(object)) {
			result.put(obj.getQualifiedName(), obj);
		}
		
		for (IEObjectDescription obj : jdtScope.getElements(object)) {
			if (!result.containsKey(obj.getQualifiedName())) {
				result.put(obj.getQualifiedName(), obj);
			}
		}	

		
		for (IEObjectDescription obj : parentScope.getElements(object)) {
			if (!result.containsKey(obj.getQualifiedName())) {
				result.put(obj.getQualifiedName(), obj);
			}
		}
		
		for (IEObjectDescription obj : super.getElements(object)) {
			if (!result.containsKey(obj.getQualifiedName())) {
				result.put(obj.getQualifiedName(), obj);
			}
		}		
		return result.values();
	}
	
	@Override
	public Iterable<IEObjectDescription> getAllElements() {
		Map<QualifiedName, IEObjectDescription> result = new HashMap<>();
		
		for (IEObjectDescription obj : getElementsFromIndex()) {
			result.put(obj.getQualifiedName(), obj);
		}
		
		for (IEObjectDescription obj : jdtScope.getAllElements()) {
			if (!result.containsKey(obj.getQualifiedName())) {
				result.put(obj.getQualifiedName(), obj);
			}
		}		
		
		for (IEObjectDescription obj : parentScope.getAllElements()) {
			if (!result.containsKey(obj.getQualifiedName())) {
				result.put(obj.getQualifiedName(), obj);
			}
		}
		
		for (IEObjectDescription obj : super.getAllElements()) {
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
		JvmType type = getTypeProvider().findTypeByName(name.toString(), binary);
		if (type == null)
			return null;
		IEObjectDescription result = EObjectDescription.create(name, type);
		return result;
	}

}
