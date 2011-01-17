package windperformancercp.views.performance;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import windperformancercp.event.IEvent;
import windperformancercp.event.IEventListener;
import windperformancercp.event.IEventType;
import windperformancercp.views.IPresenter;
import windperformancercp.views.IUserInputDialog;

public class QueryWizardDialog extends WizardDialog implements IUserInputDialog{
//public class QueryWizardDialog extends WizardDialog {
	
	private PerformanceWizardDialogPresenter presenter;
	
	public QueryWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
		this.presenter = new PerformanceWizardDialogPresenter(this);
		((QueryWizard)this.getWizard()).setAvailableMethods(presenter.getAvailableMethods());
		((QueryWizard)this.getWizard()).setAvailableSources(presenter.getAvailableSources());
		this.create();
		update();
	}

	@Override
	protected void finishPressed(){
		presenter.finishClick();
	}
	
	@Override
	protected void nextPressed() {
		//System.out.println("page changed. "+this.getCurrentPage().getName());
		if(this.getCurrentPage().getName().equals("TypeOfQueryPage")){
			presenter.typeChoosed(((QueryWizard)this.getWizard()).getQueryID(), ((QueryWizard)this.getWizard()).getMethod());
		}
		if(this.getCurrentPage().getName().equals("SourceSelectPage")){
			presenter.sourcesChoosed(((QueryWizard)this.getWizard()).getSelectedSources());
			((QueryWizard)this.getWizard()).setNeededAssignments(presenter.getNeededAssignments(null));
			((QueryWizard)this.getWizard()).setAssignmentComboElements(presenter.getPossibleAssignments());
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
	public String[] getValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Object c) {
		((QueryWizard)this.getWizard()).update();		
	}
	
	@Override
	public void update() {
		((QueryWizard)this.getWizard()).update();		
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
