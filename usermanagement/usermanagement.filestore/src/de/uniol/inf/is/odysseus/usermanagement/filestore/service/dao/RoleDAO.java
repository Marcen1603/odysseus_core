package de.uniol.inf.is.odysseus.usermanagement.filestore.service.dao;

import java.io.IOException;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.store.FileStore;
import de.uniol.inf.is.odysseus.usermanagement.AbstractStoreDAO;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.domain.Role;

public class RoleDAO extends AbstractStoreDAO<Role>{
	
	static RoleDAO dao;
	
	static synchronized public RoleDAO getInstance() throws IOException{
		if (dao == null){
			dao = new RoleDAO();
		}
		return dao;
	}
	
	RoleDAO() throws IOException {
		super(new FileStore<String, Role>(OdysseusDefaults.get("roleStoreFilename")), new ArrayList<Role>());
	}

}
