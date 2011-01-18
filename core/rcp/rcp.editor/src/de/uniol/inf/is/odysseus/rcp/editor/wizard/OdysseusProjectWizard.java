package de.uniol.inf.is.odysseus.rcp.editor.wizard;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import de.uniol.inf.is.odysseus.rcp.editor.ILogicalPlanEditorConstants;

public class OdysseusProjectWizard extends BasicNewProjectResourceWizard {

	@Override
	public boolean performFinish() {
		boolean result = super.performFinish();
		result = createFolders(getNewProject());
		result = configureProject(getNewProject());
		return result;
	}

	private boolean configureProject(IProject project) {
		try {
			IProjectDescription description = project.getDescription();
			String[] prevNatures = description.getNatureIds();
			String[] newNatures = new String[prevNatures.length + 1];
			System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
			newNatures[prevNatures.length] = ILogicalPlanEditorConstants.ODYSSEUS_PROJECT_NATURE_ID;
			description.setNatureIds(newNatures);
			project.setDescription(description, null);
		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean createFolders(IProject project) {
//		try {
//			IFolder defFolder = CubyRCPPlugIn.get().getDefinitionFolder(project);
//			defFolder.create(true, true, null);
//
//			defFolder = CubyRCPPlugIn.get().getCubesDefinitionFolder(project);
//			defFolder.create(true, true, null);
//
//		} catch (CoreException e) {
//			return false;
//		}

		return true;
	}

}
