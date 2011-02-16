package measure.windperformancercp.model;

import measure.windperformancercp.event.IEventHandler;
import measure.windperformancercp.model.sources.IDialogResult;

public interface IModel extends IEventHandler {
	//TODO: make it complete
	public int getElemCount();
	public Object getIthElement(int i);
	public void somethingChanged(IDialogResult res);

}
