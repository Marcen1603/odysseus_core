package de.uniol.inf.is.odysseus.iql.basic.scoping;

import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.scoping.impl.ImportNormalizer;


public class IQLImportNormalizer extends ImportNormalizer {

	public IQLImportNormalizer(QualifiedName importedNamespace,	boolean wildCard, boolean ignoreCase) {
		super(importedNamespace, wildCard, ignoreCase);
	}
	
	@Override
	public QualifiedName resolve(QualifiedName relativeName) {		
		if (relativeName.isEmpty())
			return null;
		if (hasWildCard()) {
			return getImportedNamespacePrefix().append(relativeName);
		} else {
			if (!isIgnoreCase()) {
				if (relativeName.getSegmentCount()==1 && relativeName.getLastSegment().equals(getImportedNamespacePrefix().getLastSegment())) {
					return getImportedNamespacePrefix();
				}
			} else {
				if (relativeName.getSegmentCount()==1 && relativeName.getLastSegment().equalsIgnoreCase(getImportedNamespacePrefix().getLastSegment())) {
					return getImportedNamespacePrefix().skipLast(1).append(relativeName.getLastSegment());
				}
			}
		}
		return null;
	}

	@Override
	public QualifiedName deresolve(QualifiedName fullyQualifiedName) {
		if (hasWildCard()) {
			if (!isIgnoreCase()) {
				if (fullyQualifiedName.startsWith(getImportedNamespacePrefix()) 
						&& fullyQualifiedName.getSegmentCount() != getImportedNamespacePrefix().getSegmentCount()) {
					return fullyQualifiedName.skipFirst(getImportedNamespacePrefix().getSegmentCount());
				}
			} else {
				if (fullyQualifiedName.startsWithIgnoreCase(getImportedNamespacePrefix()) 
					&& fullyQualifiedName.getSegmentCount() != getImportedNamespacePrefix().getSegmentCount()) {
					return fullyQualifiedName.skipFirst(getImportedNamespacePrefix().getSegmentCount());
				}
			}
		} else {
			if (!isIgnoreCase()) {
				if (fullyQualifiedName.equals(getImportedNamespacePrefix()))
					return QualifiedName.create(fullyQualifiedName.getLastSegment());
			} else {
				if (fullyQualifiedName.equalsIgnoreCase(getImportedNamespacePrefix()))
					return QualifiedName.create(fullyQualifiedName.getLastSegment());
			}
		}
		return null;
	}
}
