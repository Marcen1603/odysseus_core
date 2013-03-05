package de.uniol.inf.is.odysseus.usermanagement.mem.service.dao;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.server.store.MemoryStore;
import de.uniol.inf.is.odysseus.core.server.usermanagement.AbstractStoreDAO;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.domain.Tenant;

public class TenantDAO extends AbstractStoreDAO<Tenant> {
	
	static TenantDAO dao = null;
	
	static synchronized public TenantDAO getInstance(){
		if (dao == null){
			dao = new TenantDAO();
		}
		return dao;
	}
	
	TenantDAO(){
		super(new MemoryStore<String, Tenant>(), new ArrayList<Tenant>());
	}

}
