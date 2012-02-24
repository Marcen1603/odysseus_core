/** Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uniol.inf.is.odysseus.rcp.wizard.output;

import java.util.List;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.ScriptExecutor;
import de.uniol.inf.is.odysseus.rcp.wizard.AbstractWizard;
import de.uniol.inf.is.odysseus.rcp.wizard.AbstractWizardPage;
import de.uniol.inf.is.odysseus.rcp.wizard.output.model.OutputModel;
import de.uniol.inf.is.odysseus.rcp.wizard.output.pages.ChooseOutputTypePage;
import de.uniol.inf.is.odysseus.rcp.wizard.output.pages.CreateChartPage;
import de.uniol.inf.is.odysseus.rcp.wizard.output.pages.CreateSinkPage;
import de.uniol.inf.is.odysseus.rcp.wizard.output.pages.CreateTablePage;
import de.uniol.inf.is.odysseus.rcp.wizard.output.pages.DefineQueryPage;

/**
 * 
 * @author Dennis Geesen Created at: 01.12.2011
 */
public class OutputWizard extends AbstractWizard {

	private AbstractWizardPage<OutputWizard> defineQueryPage;
	private AbstractWizardPage<OutputWizard> choosePage;
	private AbstractWizardPage<OutputWizard> createTablePage;
	private AbstractWizardPage<OutputWizard> createChartPage;
	private AbstractWizardPage<OutputWizard> createSinkPage;
	private static final String PAGE_TITLE = "Create New Output";
	private OutputModel outputModel = new OutputModel();

	public OutputWizard() {
		setWindowTitle(PAGE_TITLE);

	}

	@Override
	public void addPages() {
		setDefineQueryPage(new DefineQueryPage(PAGE_TITLE, getFullPath()));
		addPage(getDefineQueryPage());

		choosePage = new ChooseOutputTypePage(PAGE_TITLE);
		addPage(choosePage);

		createTablePage = new CreateTablePage(PAGE_TITLE);
		addPage(createTablePage);

		createSinkPage = new CreateSinkPage(PAGE_TITLE);
		addPage(createSinkPage);

		createChartPage = new CreateChartPage(PAGE_TITLE);
		addPage(createChartPage);
	}

	public AbstractWizardPage<OutputWizard> getChoosePage() {
		return choosePage;
	}

	public void setChoosePage(AbstractWizardPage<OutputWizard> choosePage) {
		this.choosePage = choosePage;
	}

	public AbstractWizardPage<OutputWizard> getCreateTablePage() {
		return createTablePage;
	}

	public void setCreateTablePage(AbstractWizardPage<OutputWizard> createTablePage) {
		this.createTablePage = createTablePage;
	}

	public AbstractWizardPage<OutputWizard> getCreateChartPage() {
		return createChartPage;
	}

	public void setCreateChartPage(AbstractWizardPage<OutputWizard> createChartPage) {
		this.createChartPage = createChartPage;
	}

	public AbstractWizardPage<OutputWizard> getCreateSinkPage() {
		return createSinkPage;
	}

	public void setCreateSinkPage(AbstractWizardPage<OutputWizard> createSinkPage) {
		this.createSinkPage = createSinkPage;
	}

	public AbstractWizardPage<OutputWizard> getDefineQueryPage() {
		return defineQueryPage;
	}

	public void setDefineQueryPage(AbstractWizardPage<OutputWizard> defineQueryPage) {
		this.defineQueryPage = defineQueryPage;
	}

	public OutputModel getOutputModel() {
		return outputModel;
	}

	public void setOutputModel(OutputModel outputModel) {
		this.outputModel = outputModel;
	}
	
	@Override
	public boolean performFinish() {
		if (outputModel.getViewPart() instanceof AbstractChart<?, ?>) {
			AbstractChart<?, ?> chart = (AbstractChart<?, ?>) outputModel.getViewPart();			
//			List<ISource<?>> sources = ScriptExecutor.loadAndExecuteQueryScript(outputModel.getQueryFile());
//			if(sources.size()<1){
//				MessageBox mb = new MessageBox(getShell(), SWT.ICON_ERROR);
//				mb.setMessage("Your query file don't have genereated any sources where the view can connect to");
//				mb.setText("No sources found");
//				mb.open();
//				return false;
//			}else{
//				openView(chart);
//			}
			openView(chart);
			return true;
		}
		return false;
	}

	public AbstractChart<?, ?> openView(AbstractChart<?, ?> createView) {
		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {			
			String secondaryIdentifier = AbstractChart.getUniqueSecondIdentifier(createView.getViewID());
			AbstractChart<?, ?> view = (AbstractChart<?, ?>) activePage.showView(createView.getViewID(), secondaryIdentifier, IWorkbenchPage.VIEW_ACTIVATE);
			List<ISource<?>> sources = ScriptExecutor.loadAndExecuteQueryScript(this.outputModel.getQueryFile());
			view.setFileName(this.outputModel.getQueryFile());
			view.initWithOperator(sources.get(0));
			return view;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
