package measure.windperformancercp.views;

import measure.windperformancercp.event.EventHandler;
import measure.windperformancercp.event.IEvent;
import measure.windperformancercp.event.IEventListener;
import measure.windperformancercp.event.IEventType;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.widgets.Shell;


public class AbstractUIDialog extends TitleAreaDialog implements IUserInputDialog{

	
	public AbstractUIDialog(Shell parentShell) {
		super(parentShell);
	}

	/*public AbstractUIDialog(IShellProvider parentShell) {
		super(parentShell);
	}*/
	
	//	Event handling
	EventHandler eventHandler = new EventHandler();
	
	@Override
	public void subscribe(IEventListener listener, IEventType type) {
		eventHandler.subscribe(listener, type);
	}

	@Override
	public void unsubscribe(IEventListener listener, IEventType type) {
		eventHandler.unsubscribe(listener, type);

	}

	@Override
	public void subscribeToAll(IEventListener listener) {
		eventHandler.subscribeToAll(listener);

	}

	@Override
	public void unSubscribeFromAll(IEventListener listener) {
		eventHandler.unSubscribeFromAll(listener);
	}

	@Override
	public void fire(IEvent<?, ?> event) {
		eventHandler.fire(event);
	}

	@Override
	public AbstractUIDialog getInstance(){
		return this;
	}
	
	public void resetView(){}
	
	
	@Override
	public void okPressed(){
		close();
	}
	
	@Override
	public void cancelPressed(){
		close();
	}
	
	@Override
	public void update(Object c){}

	@Override
	public void setInput(Object input) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void setContent(Object input){
		// TODO Auto-generated method stub
	}

	@Override
	public IPresenter getPresenter() {
		// TODO Auto-generated method stub
		return null;
	}


}
