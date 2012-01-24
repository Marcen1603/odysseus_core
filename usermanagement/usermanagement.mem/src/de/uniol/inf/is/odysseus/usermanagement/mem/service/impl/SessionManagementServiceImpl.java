package de.uniol.inf.is.odysseus.usermanagement.mem.service.impl;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.usermanagement.AbstractSessionManagement;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.dao.UserDAO;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.domain.User;

public class SessionManagementServiceImpl extends AbstractSessionManagement<User>{ 
	
	protected void activate(ComponentContext context) {		
		userDAO = UserDAO.getInstance();
	}

	
}
