package de.uniol.inf.is.odysseus.iql.basic.ui.scoping;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.xtext.common.types.access.impl.IndexedJvmTypeAccess;
import org.eclipse.xtext.common.types.access.jdt.JdtTypeProvider;

import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLJdtTypeProvider;

@SuppressWarnings("restriction")
public class IQLJdtTypeProvider extends JdtTypeProvider implements IIQLJdtTypeProvider {

	@SuppressWarnings("deprecation")
	public IQLJdtTypeProvider(IJavaProject javaProject,
			ResourceSet resourceSet, IndexedJvmTypeAccess indexedJvmTypeAccess,
			WorkingCopyOwner workingCopyOwner) {
		super(javaProject, resourceSet, indexedJvmTypeAccess, workingCopyOwner);
	}

}
