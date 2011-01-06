package windperformancercp.event;

import windperformancercp.model.IDialogResult;
import windperformancercp.views.AbstractUIDialog;

public class InputDialogEvent extends AbstractEvent<AbstractUIDialog, String[]> {
//public class InputDialogEvent extends AbstractEvent<AbstractUIDialog, IDialogResult> {
	
	public InputDialogEvent(AbstractUIDialog source, InputDialogEventType type, String[] value) {
		super(source,type,value);
	}
	
	public AbstractUIDialog getDialog(){
		return getSender();
	}
	
	public IEventType getAttEventType() {
		return (InputDialogEventType) getEventType();
	}

}
