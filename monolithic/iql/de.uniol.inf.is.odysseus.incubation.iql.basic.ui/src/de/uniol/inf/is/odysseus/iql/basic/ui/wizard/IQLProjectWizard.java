package de.uniol.inf.is.odysseus.iql.basic.ui.wizard;

import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.xtext.ui.wizard.IProjectInfo;
import org.eclipse.xtext.ui.wizard.IProjectCreator;
import com.google.inject.Inject;

public class IQLProjectWizard extends org.eclipse.xtext.ui.wizard.XtextNewProjectWizard {

	private WizardNewProjectCreationPage mainPage;

	@Inject
	public IQLProjectWizard(IProjectCreator projectCreator) {
		super(projectCreator);
		setWindowTitle("New IQL Project");
	}

	public void addPages() {
		mainPage = new WizardNewProjectCreationPage("basicNewProjectPage");
		mainPage.setTitle("IQL Project");
		mainPage.setDescription("Create a new IQL project.");
		addPage(mainPage);
	}

	@Override
	protected IProjectInfo getProjectInfo() {
		IQLProjectInfo projectInfo = new IQLProjectInfo();
		projectInfo.setProjectName(mainPage.getProjectName());
		return projectInfo;
	}

}
