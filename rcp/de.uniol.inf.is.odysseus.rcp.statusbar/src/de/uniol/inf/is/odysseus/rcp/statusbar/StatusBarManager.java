package de.uniol.inf.is.odysseus.rcp.statusbar;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.StatusLineContributionItem;

public class StatusBarManager {

	private static StatusBarManager instance = null;
	
	public static final String EXECUTOR_ID = "executorStatus";
	public static final String SCHEDULER_ID = "schedulerStatus";
	
	private IStatusLineManager manager;
	
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
	}
	
	public void setMessage( String itemID, String message ) {
		IContributionItem[] items = manager.getItems();
		for( IContributionItem i : items ) {
			if( i.getId().equals(itemID)) {
				((StatusLineContributionItem)i).setText(message);
			}
		}
	}
	
	public void setMessage( String message ) {
		manager.setMessage(message);
	}
}
