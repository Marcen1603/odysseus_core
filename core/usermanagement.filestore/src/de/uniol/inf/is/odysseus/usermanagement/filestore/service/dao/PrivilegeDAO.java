package de.uniol.inf.is.odysseus.usermanagement.filestore.service.dao;

import java.io.IOException;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.store.FileStore;
import de.uniol.inf.is.odysseus.core.server.usermanagement.AbstractStoreDAO;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.domain.Privilege;

public class PrivilegeDAO extends AbstractStoreDAO<Privilege> {

	static PrivilegeDAO dao;
	
	static synchronized public PrivilegeDAO getInstance() throws IOException{
		if (dao == null){
			dao = new PrivilegeDAO();
		}
		return dao;
	}

	
	PrivilegeDAO() throws IOException {
		super(new FileStore<String, Privilege>(OdysseusConfiguration.get("privilegStoreFilename")), new ArrayList<Privilege>());
	}
}
