package de.uniol.inf.is.odysseus.rcp.editor.nature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;

public class OdysseusProjectNature implements IProjectNature {

	private IProject project;
	
	@Override
	public void configure() throws CoreException {

	}

	@Override
	public void deconfigure() throws CoreException {

	}

	@Override
	public IProject getProject() {
		return project;
	}

	@Override
	public void setProject(IProject project) {
		Assert.isNotNull(project, "project");
		
		this.project = project;
	}

}
