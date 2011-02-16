package measure.windperformancercp.views;

import measure.windperformancercp.event.IEventHandler;

public interface IUserInputDialog extends IEventHandler {
	public IUserInputDialog getInstance();
	public void update(Object c);
	public void setInput(Object input);
	public void setContent(Object input);
	public IPresenter getPresenter();

}
