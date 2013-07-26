package de.uniol.inf.is.odysseus.rcp.dashboard.windows;

import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

class ImageFileContentProvider extends BaseWorkbenchContentProvider {
	
	private static final List<String> ACCEPTED_FILE_EXTENSIONS = new ImmutableList.Builder<String>()
		.add("png")
		.add("jpg")
		.add("gif")
		.build();
	
	@Override
	public Object[] getChildren(Object element) {
		if (element instanceof IContainer) {
			Optional<IResource[]> optMembers = tryGetMembers((IContainer)element);
			return optMembers.isPresent() ? collectImageFilesAndContainers(optMembers.get()).toArray() : new Object[0];
		}
		return super.getChildren(element);
	}

	private static List<IResource> collectImageFilesAndContainers(IResource[] optMembers) {
		List<IResource> result = Lists.newArrayList();
		for (int i = 0; i < optMembers.length; i++) {
			IResource member = optMembers[i];
			
			if( isImageOrContainer(member)) {
				result.add(member);
			}
		}
		return result;
	}

	private static boolean isImageOrContainer(IResource member) {
		return  member instanceof IContainer || ( member instanceof IFile && isImageFile((IFile)member));
	}

	private static boolean isImageFile(IFile member) {
		return ACCEPTED_FILE_EXTENSIONS.contains(member.getFileExtension());
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
