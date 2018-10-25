package de.uniol.inf.is.odysseus.rcp.dashboard.windows;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;

import com.google.common.base.Optional;
import java.util.Objects;
import com.google.common.collect.Lists;

class FileContentProvider extends BaseWorkbenchContentProvider {
	
	private final Collection<String> acceptedFileExtensions;
	
	public FileContentProvider( Collection<String> acceptedFileExtensions ) {
		Objects.requireNonNull(acceptedFileExtensions, "List of accepted file extensions must not be null");
		
		this.acceptedFileExtensions = acceptedFileExtensions;
	}
	
	@Override
	public Object[] getChildren(Object element) {
		if (element instanceof IContainer) {
			Optional<IResource[]> optMembers = tryGetMembers((IContainer)element);
			return optMembers.isPresent() ? collectAcceptedFilesAndContainers(optMembers.get()).toArray() : new Object[0];
		}
		return super.getChildren(element);
	}

	private List<IResource> collectAcceptedFilesAndContainers(IResource[] optMembers) {
		List<IResource> result = Lists.newArrayList();
		for (int i = 0; i < optMembers.length; i++) {
			IResource member = optMembers[i];
			
			if( isAcceptedExtensionOrContainer(member)) {
				result.add(member);
			}
		}
		return result;
	}

	private boolean isAcceptedExtensionOrContainer(IResource member) {
		return  member instanceof IContainer || ( member instanceof IFile && isAcceptedExtension((IFile)member));
	}

	private boolean isAcceptedExtension(IFile member) {
		return acceptedFileExtensions.contains(member.getFileExtension());
	}

	private static Optional<IResource[]> tryGetMembers(IContainer element) {
		try {
			IResource[] members = element.members();
			return Optional.of(members);
		} catch (CoreException e) {
			return Optional.absent();
		}
	}
}
