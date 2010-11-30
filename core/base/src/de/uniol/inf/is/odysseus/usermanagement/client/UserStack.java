package de.uniol.inf.is.odysseus.usermanagement.client;

import java.util.Stack;

import de.uniol.inf.is.odysseus.usermanagement.User;

public class UserStack {

	private static Stack<User> users = new Stack<User>();
	
	private UserStack() {
	}
	
	public static void push( User user ) {
		users.add(user);
	}
	
	public static User pop() {
		return users.pop();
	}
	
	public static void clear() {
		users.clear();
	}
	
	public static boolean isEmpty() {
		return users.isEmpty();
	}
}
