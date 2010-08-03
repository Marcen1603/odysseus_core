package de.uniol.inf.is.odysseus.rcp.statusbar;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.StatusLineContributionItem;
import org.eclipse.ui.PlatformUI;

public class StatusBarManager {

	private static StatusBarManager instance = null;
	
	public static final String EXECUTOR_ID = "executorStatus";
	public static final String SCHEDULER_ID = "schedulerStatus";
	
	private IStatusLineManager manager;
	
	private Map<String, String> cache = new HashMap<String, String>();
	private String msgCache;
	
	private StatusBarManager() {
		
	}
	
	public static StatusBarManager getInstance() {
		if( instance == null ) 
			instance = new StatusBarManager();
		return instance;
	}
	
	public void setStatusLineManager( IStatusLineManager manager ) {
		this.manager = manager;
		
		StatusLineContributionItem item = new StatusLineContributionItem(EXECUTOR_ID);
		StatusLineContributionItem item2 = new StatusLineContributionItem(SCHEDULER_ID);
		manager.add(item);	
		manager.add(item2);
		
		setStandardMessages();
		applyCache();
	}
	
	private void setStandardMessages() {
		setMessage(StatusBarManager.EXECUTOR_ID, "No executor found");
		setMessage(StatusBarManager.SCHEDULER_ID, "Scheduler stopped");
	}
	
	private void applyCache() {
		if(msgCache != null )
			setMessage(msgCache);
		
		for( String key : cache.keySet() ) 
			setMessage(key, cache.get(key));
		cache.clear();
	}
	
	public void setMessage( final String itemID, final String message ) {
		if( manager == null ) {
			cache.put(itemID, message);
			return;
		}
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				IContributionItem[] items = manager.getItems();
				for( IContributionItem i : items ) {
					if( i.getId().equals(itemID)) {
						((StatusLineContributionItem)i).setText(message);
					}
				}
			}
			
		});
	}
	
	public void setMessage( final String message ) {
		if( manager == null ) {
			msgCache = message;
			return;
		}
		
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {

				manager.setMessage(message);
			}
		});
	}
}
