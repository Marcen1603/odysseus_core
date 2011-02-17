/** Copyright [2011] [The Odysseus Team]
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
package measure.windperformancercp.views.performance;

import measure.windperformancercp.event.IEvent;
import measure.windperformancercp.event.IEventListener;
import measure.windperformancercp.event.IEventType;
import measure.windperformancercp.views.IPresenter;
import measure.windperformancercp.views.IUserInputDialog;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import measure.windperformancercp.views.performance.PerformanceWizard.AssignAttributePage;

public class PerformanceWizardDialog extends WizardDialog implements IUserInputDialog{
	
	private PerformanceWizardDialogPresenter presenter;
	
	/**
	 * The Performance Wizard Dialog. In Reality a wrapper for the wizard, 
	 * forwards wishes and modifications from the wizard and commands from the controller.
	 * @param parentShell
	 * @param newWizard
	 */
	public PerformanceWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
		this.presenter = new PerformanceWizardDialogPresenter(this);
		((PerformanceWizard)this.getWizard()).setAvailableMethods(presenter.getAvailableMethods());
		((PerformanceWizard)this.getWizard()).setAvailableSources(presenter.getAvailableSources());
		this.create();
		update();
	}

	@Override
	protected void finishPressed(){
		presenter.assignmentsMade(((PerformanceWizard)this.getWizard()).getSelectedAssignments(), ((PerformanceWizard)this.getWizard()).getTau());
		presenter.finishClick();
	}
	
	/**
	 * Override the super method
	 * Sets the content for the pages and forwards the wizards information to the controller
	 */
	@Override
	protected void nextPressed() {
		//System.out.println("page changed. "+this.getCurrentPage().getName());
		if(this.getCurrentPage().getName().equals("TypeOfQueryPage")){
			presenter.typeChoosed(((PerformanceWizard)this.getWizard()).getQueryID(), ((PerformanceWizard)this.getWizard()).getMethod());
		}
		if(this.getCurrentPage().getName().equals("SourceSelectPage")){
			presenter.sourcesChoosed(((PerformanceWizard)this.getWizard()).getSelectedSources());
			((PerformanceWizard)this.getWizard()).setNeededAssignments(presenter.getNeededAssignments(null));
			((PerformanceWizard)this.getWizard()).setSelectedAssignments(presenter.getNeededAssignments(null));
			((PerformanceWizard)this.getWizard()).setAssignmentComboElements(presenter.getPossibleAssignments());
			((AssignAttributePage)this.getCurrentPage().getNextPage()).onEnterPage();
		}
		  super.nextPressed();
	}

	@Override
	public void subscribe(IEventListener listener, IEventType type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unsubscribe(IEventListener listener, IEventType type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subscribeToAll(IEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unSubscribeFromAll(IEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fire(IEvent<?, ?> event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IUserInputDialog getInstance() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public void update(Object c) {
		((PerformanceWizard)this.getWizard()).update();		
	}
	
	@Override
	public void update() {
		((PerformanceWizard)this.getWizard()).update();		
	}

	@Override
	public void setInput(Object input) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setContent(Object input) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IPresenter getPresenter() {
		// TODO Auto-generated method stub
		return this.presenter;
	}
	

}
