package measure.windperformancercp.views;

import measure.windperformancercp.event.IEventHandler;
import measure.windperformancercp.model.sources.IDialogResult;

public interface IPresenter extends IEventHandler{
	public void feedDialog(IDialogResult input);
}
