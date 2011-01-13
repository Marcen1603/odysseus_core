package windperformancercp.views;

import windperformancercp.event.IEventHandler;
import windperformancercp.model.sources.IDialogResult;

public interface IPresenter extends IEventHandler{
	public void feedDialog(IDialogResult input);
}
