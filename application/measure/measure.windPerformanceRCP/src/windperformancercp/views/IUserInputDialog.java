package windperformancercp.views;

import windperformancercp.event.IEventHandler;

public interface IUserInputDialog extends IEventHandler {
	public IUserInputDialog getInstance();
	public String[] getValues();
	public void update(Object c);

}
