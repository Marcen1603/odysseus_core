package de.uniol.inf.is.odysseus.usermanagement.filestore.service.dao;

import java.io.IOException;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.store.FileStore;
import de.uniol.inf.is.odysseus.core.server.usermanagement.AbstractStoreDAO;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.domain.Tenant;

public class TenantDAO extends AbstractStoreDAO<Tenant> {

	static TenantDAO dao = null;
	
	static synchronized public TenantDAO getInstance() throws IOException{
		if (dao == null){
			dao = new TenantDAO();
		}
		return dao;
	}
	
	TenantDAO() throws IOException {
		super(new FileStore<String, Tenant>(OdysseusConfiguration.get("tenantStoreFilename")), new ArrayList<Tenant>());
	}

}
