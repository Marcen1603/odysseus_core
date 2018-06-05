package de.uniol.inf.is.odysseus.iql.basic.ui.scoping;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.core.DefaultWorkingCopyOwner;
import org.eclipse.xtext.common.types.access.jdt.IJdtTypeProvider;
import org.eclipse.xtext.common.types.access.jdt.IWorkingCopyOwnerProvider;
import org.eclipse.xtext.common.types.access.jdt.JdtTypeProviderFactory;

import com.google.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLJdtTypeProvider;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLJdtTypeProviderFactory;

@SuppressWarnings("restriction")
public class IQLJdtTypeProviderFactory extends JdtTypeProviderFactory implements IIQLJdtTypeProviderFactory {
	
	@Inject
	private IWorkingCopyOwnerProvider copyOwnerProvider;
	
	@Override
	protected IJdtTypeProvider createJdtTypeProvider(IJavaProject javaProject, ResourceSet resourceSet) {
		if (javaProject == null)
			return new IQLNullJdtTypeProvider(resourceSet);
		return new IQLJdtTypeProvider(javaProject, resourceSet, getIndexedJvmTypeAccess(), copyOwnerProvider==null? DefaultWorkingCopyOwner.PRIMARY : copyOwnerProvider.getWorkingCopyOwner(javaProject, resourceSet));
	}

	@Override
	public IIQLJdtTypeProvider createJdtTypeProvider(ResourceSet resourceSet) {
		return (IIQLJdtTypeProvider) super.createTypeProvider(resourceSet);
	}
}
