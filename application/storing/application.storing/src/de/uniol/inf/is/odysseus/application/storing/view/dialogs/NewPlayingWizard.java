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

package de.uniol.inf.is.odysseus.application.storing.view.dialogs;

import org.eclipse.jface.wizard.IWizardPage;

import de.uniol.inf.is.odysseus.rcp.wizard.AbstractWizard;

/**
 * 
 * @author Dennis Geesen
 * Created at: 29.11.2011
 */
public class NewPlayingWizard extends AbstractWizard{

	private IWizardPage choosePage;
	private IWizardPage createTablePage;
	private IWizardPage createChartPage;
	private IWizardPage createSinkPage;
	
	public NewPlayingWizard() {
		setWindowTitle("Create New Playback");
		
	}
	
	@Override
	public void addPages() {
		choosePage = new NewPlayingWizardChoosePage();
		addPage(choosePage);
		
		createTablePage = new NewPlayingWizardCreateTablePage();
		addPage(createTablePage);
		
		createSinkPage = new NewPlayingWizardCreateSinkPage();
		addPage(createSinkPage);
		
		createChartPage = new NewPlayingWizardCreateChartPage();
		addPage(createChartPage);
	}
	
	

	public IWizardPage getChoosePage() {
		return choosePage;
	}

	public void setChoosePage(IWizardPage choosePage) {
		this.choosePage = choosePage;
	}

	public IWizardPage getCreateTablePage() {
		return createTablePage;
	}

	public void setCreateTablePage(IWizardPage createTablePage) {
		this.createTablePage = createTablePage;
	}

	public IWizardPage getCreateChartPage() {
		return createChartPage;
	}

	public void setCreateChartPage(IWizardPage createChartPage) {
		this.createChartPage = createChartPage;
	}

	public IWizardPage getCreateSinkPage() {
		return createSinkPage;
	}

	public void setCreateSinkPage(IWizardPage createSinkPage) {
		this.createSinkPage = createSinkPage;
	}

	@Override
	public boolean performFinish() {	
		return false;
	}


}
