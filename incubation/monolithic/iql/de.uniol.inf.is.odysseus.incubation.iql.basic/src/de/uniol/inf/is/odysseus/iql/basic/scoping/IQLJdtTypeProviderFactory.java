package de.uniol.inf.is.odysseus.iql.basic.scoping;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.core.DefaultWorkingCopyOwner;
import org.eclipse.xtext.common.types.access.jdt.IJdtTypeProvider;
import org.eclipse.xtext.common.types.access.jdt.IWorkingCopyOwnerProvider;
import org.eclipse.xtext.common.types.access.jdt.JdtTypeProviderFactory;
import org.eclipse.xtext.common.types.access.jdt.NullJdtTypeProvider;

import com.google.inject.Inject;

@SuppressWarnings("restriction")
public class IQLJdtTypeProviderFactory extends JdtTypeProviderFactory {
	@Inject
	private IWorkingCopyOwnerProvider copyOwnerProvider;
	
	@Override
	protected IJdtTypeProvider createJdtTypeProvider(IJavaProject javaProject, ResourceSet resourceSet) {
		if (javaProject == null)
			return new NullJdtTypeProvider(resourceSet);
		return new IQLJdtTypeProvider(javaProject, resourceSet, getIndexedJvmTypeAccess(), copyOwnerProvider==null? DefaultWorkingCopyOwner.PRIMARY : copyOwnerProvider.getWorkingCopyOwner(javaProject, resourceSet));
	}
}
