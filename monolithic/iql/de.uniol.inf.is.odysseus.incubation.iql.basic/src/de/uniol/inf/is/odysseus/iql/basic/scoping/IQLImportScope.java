package de.uniol.inf.is.odysseus.iql.basic.scoping;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.ISelectable;
import org.eclipse.xtext.resource.impl.AliasedEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.impl.ImportNormalizer;
import org.eclipse.xtext.scoping.impl.ImportScope;

public class IQLImportScope extends ImportScope {
	private List<ImportNormalizer> normalizers;
	private EClass type;
	
	
	public IQLImportScope(List<ImportNormalizer> normalizers,
			IScope parent, ISelectable importFrom, EClass type,
			boolean ignoreCase) {
		super(normalizers, parent, importFrom, type, ignoreCase);
		this.normalizers = normalizers;
		this.type = type;
	}
	
	public boolean hasConflict(QualifiedName name) {
		List<IEObjectDescription> result = newArrayList();
		QualifiedName resolvedQualifiedName = null;
		ISelectable importFrom = getImportFrom();
		for (ImportNormalizer normalizer : normalizers) {
			final QualifiedName resolvedName = normalizer.resolve(name);
			if (resolvedName != null) {
				Iterable<IEObjectDescription> resolvedElements = importFrom.getExportedObjects(type, resolvedName,
						isIgnoreCase());
				for (IEObjectDescription resolvedElement : resolvedElements) {
					if (resolvedQualifiedName == null)
						resolvedQualifiedName = resolvedName;
					else if (!resolvedQualifiedName.equals(resolvedName)) {
						if (result.get(0).getEObjectOrProxy() != resolvedElement.getEObjectOrProxy()) {
							return true;
						}
					}
					QualifiedName alias = normalizer.deresolve(resolvedElement.getName());
					final AliasedEObjectDescription aliasedEObjectDescription = new AliasedEObjectDescription(alias,resolvedElement);
					result.add(aliasedEObjectDescription);
				}
			}
		}
		return false;
	} 
	
}
