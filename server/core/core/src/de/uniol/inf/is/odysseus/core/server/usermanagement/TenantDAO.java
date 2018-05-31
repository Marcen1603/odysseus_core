package de.uniol.inf.is.odysseus.core.server.usermanagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.store.FileStore;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.core.usermanagement.Tenant;

public class TenantDAO extends AbstractStoreDAO<ITenant> {

	static Logger logger = LoggerFactory.getLogger(TenantDAO.class);
	/** use osgi injection instead */
	@Deprecated
	static TenantDAO instance = null;

	@Deprecated
	static synchronized public TenantDAO getInstance() {
		return instance;
	}

	private OdysseusConfiguration config;

	public void setInstance() throws IOException {
		instance = this;
		init(new FileStore<String, ITenant>(config.getFileProperty("tenantStoreFilename")), new ArrayList<ITenant>());
		if (findAll().isEmpty()) {
			ITenant t = new Tenant();
			t.setName(config.get("Tenant.DefaultName"));
			create(t);

		} else {
			if (logger.isDebugEnabled()) {
				List<ITenant> all = findAll();
				logger.trace("Tenants loaded " + all);
			}
		}

	}

	public void setConfig(OdysseusConfiguration config) {
		this.config = config;
	}

}
