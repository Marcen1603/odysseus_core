package de.uniol.inf.is.odysseus.usermanagement.filestore.service.impl;

import java.io.IOException;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.usermanagement.AbstractSessionManagement;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.dao.UserDAO;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.domain.User;

public class SessionManagementServiceImpl extends AbstractSessionManagement<User>{ 
	
	protected void activate(ComponentContext context) {		
		try {
			userDAO = UserDAO.getInstance();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}
