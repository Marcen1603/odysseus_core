package windperformancercp.views;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import windperformancercp.event.EventHandler;
import windperformancercp.event.IEvent;
import windperformancercp.event.IEventListener;
import windperformancercp.event.IEventType;

public class AbstractUIDialog extends Dialog implements IUserInputDialog{

	Composite par;
	String title;
	
	public AbstractUIDialog(Shell parentShell) {
		super(parentShell);
	}

	public AbstractUIDialog(IShellProvider parentShell) {
		super(parentShell);
	}
	
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
	
	public String[] getValues(){return new String[]{};}
	
	@Override
	public void okPressed(){
		//TODO
		System.out.println(getInstance().toString()+": Ok gedrueckt!");
		close();
	}
	
	@Override
	public void cancelPressed(){
		System.out.println(getInstance().toString()+": Cancel gedrueckt!");
		close();
	}
	
	@Override
	public void update(Object c){}


}
