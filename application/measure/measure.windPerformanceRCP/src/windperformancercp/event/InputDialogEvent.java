package windperformancercp.event;

import windperformancercp.model.sources.IDialogResult;
import windperformancercp.views.IPresenter;

public class InputDialogEvent extends AbstractEvent<IPresenter, IDialogResult> {
//public class InputDialogEvent extends AbstractEvent<AbstractUIDialog, IDialogResult> {
	
	public InputDialogEvent(IPresenter source, InputDialogEventType type, IDialogResult value) {
	//public InputDialogEvent(AbstractUIDialog source, InputDialogEventType type, IDialogResult value) {
		super(source,type,value);
	}
	
	public IPresenter getPresenter(){
		return getSender();
	}
	
	public IEventType getAttEventType() {
		return (InputDialogEventType) getEventType();
	}
	
	public IDialogResult getResult(){
		return (IDialogResult) getValue();
	}

}
