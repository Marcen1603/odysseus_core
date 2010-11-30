package de.uniol.inf.is.odysseus.usermanagement.client;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.usermanagement.User;

public class ActiveUser {
	
	private static List<IActiveUserListener> activeUserListener = new CopyOnWriteArrayList<IActiveUserListener>();
	
	private static User activeUser; 
	
	private ActiveUser() {
	}
	
	public synchronized static void setActiveUser( User user ) {
		activeUser = user;
		fire();
	}
	
	public synchronized static User getActiveUser() { 
		return activeUser;
	}
	
	public static void addActiveUserListner(IActiveUserListener listener){
		activeUserListener.add(listener);
	}
	
	public static void removeActiveUserListner(IActiveUserListener listener){
		activeUserListener.remove(listener);
	}
	
	public static void fire(){
		for (IActiveUserListener l: activeUserListener){
			l.activeUserChanged(activeUser);
		}
	}
	
}
