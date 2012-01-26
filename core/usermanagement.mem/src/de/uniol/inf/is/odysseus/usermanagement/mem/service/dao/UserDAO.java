package de.uniol.inf.is.odysseus.usermanagement.mem.service.dao;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.store.MemoryStore;
import de.uniol.inf.is.odysseus.usermanagement.AbstractStoreDAO;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.domain.User;

public class UserDAO extends AbstractStoreDAO<User>{
	
	static UserDAO userDAO;
	
	static synchronized public UserDAO getInstance(){
		if (userDAO == null){
			userDAO = new UserDAO();
		}
		return userDAO;
	}
		
	UserDAO() {
		super(new MemoryStore<String, User>(), new ArrayList<User>());
	}

}
