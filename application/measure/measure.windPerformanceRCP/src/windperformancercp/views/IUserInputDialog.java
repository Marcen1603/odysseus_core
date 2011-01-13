package windperformancercp.views;

import windperformancercp.event.IEventHandler;

public interface IUserInputDialog extends IEventHandler {
	public IUserInputDialog getInstance();
	public String[] getValues();
	public void update(Object c);
	public void setInput(Object input);
	public void setContent(Object input);
	public IPresenter getPresenter();

}
