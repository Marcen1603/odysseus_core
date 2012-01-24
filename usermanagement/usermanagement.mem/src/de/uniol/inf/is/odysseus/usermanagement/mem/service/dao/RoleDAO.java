package de.uniol.inf.is.odysseus.usermanagement.mem.service.dao;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.store.MemoryStore;
import de.uniol.inf.is.odysseus.usermanagement.AbstractStoreDAO;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.domain.Role;

public class RoleDAO extends AbstractStoreDAO<Role>{
	
	static RoleDAO dao;
	
	static synchronized public RoleDAO getInstance(){
		if (dao == null){
			dao = new RoleDAO();
		}
		return dao;
	}
	
	RoleDAO() {
		super(new MemoryStore<String, Role>(), new ArrayList<Role>());
	}

}
