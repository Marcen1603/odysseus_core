package de.uniol.inf.is.odysseus.usermanagement.filestore.service.dao;

import java.io.IOException;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.store.FileStore;
import de.uniol.inf.is.odysseus.usermanagement.AbstractStoreDAO;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.domain.User;

public class UserDAO extends AbstractStoreDAO<User>{
	
	static UserDAO userDAO;
	
	static synchronized public UserDAO getInstance() throws IOException{
		if (userDAO == null){
			userDAO = new UserDAO();
		}
		return userDAO;
	}
		
	UserDAO() throws IOException {
		super(new FileStore<String, User>(OdysseusDefaults.get("userStoreFilename")), new ArrayList<User>());
	}

}
