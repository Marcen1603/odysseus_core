package de.uniol.inf.is.odysseus.core.server.usermanagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.store.FileStore;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;

public class TenantDAO extends AbstractStoreDAO<ITenant> {

	static Logger logger = LoggerFactory.getLogger(TenantDAO.class);
	
	static TenantDAO dao = null;
	
	static synchronized public TenantDAO getInstance(){
		if (dao == null){
			try {
				dao = new TenantDAO();
				if (dao.findAll().isEmpty()){
					ITenant t = new Tenant();
					t.setName(OdysseusConfiguration.get("Tenant.DefaultName"));
					dao.create(t);
					
					//TODO: REMOVE ME (Only for Debugging)
					
					t = new Tenant();
					t.setName("Marco1");
					dao.create(t);
					t = new Tenant();
					t.setName("Marco2");
					dao.create(t);
					t = new Tenant();
					t.setName("Marco3");
					dao.create(t);
					
				}else{
					if (logger.isDebugEnabled()){
						List<ITenant> all = dao.findAll();
						logger.trace("Tenants loaded "+all);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dao;
	}
	
	TenantDAO() throws IOException {
		super(new FileStore<String, ITenant>(OdysseusConfiguration.getFileProperty("tenantStoreFilename")), new ArrayList<ITenant>());
	}

}
