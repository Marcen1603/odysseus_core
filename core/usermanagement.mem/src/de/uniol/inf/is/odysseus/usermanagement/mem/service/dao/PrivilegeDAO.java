package de.uniol.inf.is.odysseus.usermanagement.mem.service.dao;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.server.store.MemoryStore;
import de.uniol.inf.is.odysseus.core.server.usermanagement.AbstractStoreDAO;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.domain.Privilege;

public class PrivilegeDAO extends AbstractStoreDAO<Privilege> {

	static PrivilegeDAO dao;
	
	static synchronized public PrivilegeDAO getInstance(){
		if (dao == null){
			dao = new PrivilegeDAO();
		}
		return dao;
	}

	
	PrivilegeDAO() {
		super(new MemoryStore<String, Privilege>(), new ArrayList<Privilege>());
	}
}
