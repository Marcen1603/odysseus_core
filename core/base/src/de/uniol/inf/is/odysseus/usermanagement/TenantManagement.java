package de.uniol.inf.is.odysseus.usermanagement;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.store.FileStore;
import de.uniol.inf.is.odysseus.store.IStore;
import de.uniol.inf.is.odysseus.store.MemoryStore;
import de.uniol.inf.is.odysseus.store.StoreException;

public class TenantManagement {

	// TODO: Rechtemanagement

	static private TenantManagement instance = null;
	
	private List<ITenantManagementListener> listeners = new CopyOnWriteArrayList<ITenantManagementListener>();

	public void addTenantManagementListener(ITenantManagementListener l){
		listeners.add(l);		
	}

	public void removeTenantManagementListener(ITenantManagementListener l){
		listeners.remove(l);		
	}
	
	public void fireTenantManagementListener(){
		for (ITenantManagementListener l:listeners){
			l.tenantsChangedEvent();
		}
	}

	public synchronized static TenantManagement getInstance() {
		if (instance == null) {
			instance = new TenantManagement();

		}
		return instance;
	}

	final private IStore<String, Tenant> registeredTenants;
	final private IStore<String, Tenant> users;
	final private IStore<String, IServiceLevelAgreement> slas;

	private TenantManagement() {
		try {
			if (Boolean.parseBoolean(OdysseusDefaults.get("storeTenants"))){
				registeredTenants = new FileStore<String, Tenant>(OdysseusDefaults.get("tenantsFilename"));
				users = new FileStore<String, Tenant>(OdysseusDefaults.get("userTenantFilename"));
				slas = new FileStore<String, IServiceLevelAgreement>(OdysseusDefaults.get("slasFilename"));
			}else{
				registeredTenants = new MemoryStore<String, Tenant>();
				users = new MemoryStore<String, Tenant>();
				slas = new MemoryStore<String, IServiceLevelAgreement>();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void addSLA(String name, IServiceLevelAgreement sla) {
		try {
			slas.put(name, sla);
		} catch (StoreException e) {
			throw new RuntimeException(e);
		}
		fireTenantManagementListener();
	}

	public void createTenant(String name, String slaName, User caller) {
		IServiceLevelAgreement sla = slas.get(slaName);
		if (sla != null) {
			Tenant newT = new Tenant(name, sla);

			try {
				registeredTenants.put(name, newT);
			} catch (StoreException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new RuntimeException("No SLA with name " + slaName + " found");
		}
		fireTenantManagementListener();
	}

	public void addUserToTenant(Tenant tenant, User user, User caller)
			throws TenantNotFoundException, TooManyUsersException {
		addUserToTenant(tenant.getName(), user, caller);
	}

	public void addUserToTenant(String tenantName, User user, User caller)
			throws TenantNotFoundException, TooManyUsersException {
		Tenant t = registeredTenants.get(tenantName);
		if (t != null) {
			t.addUser(user);
			try {
				users.put(user.getUsername(), t);
			} catch (StoreException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new TenantNotFoundException(tenantName);
		}
		fireTenantManagementListener();
	}

	public void removeUserFromTenant(Tenant tenant, User user, User caller)
			throws TenantNotFoundException {
		removeUserFromTenant(tenant.getName(), user, caller);
	}

	public void removeUserFromTenant(String tenantName, User user, User caller)
			throws TenantNotFoundException {
		Tenant t = registeredTenants.get(tenantName);
		if (t != null) {
			t.removeUser(user);
			try {
				users.remove(user.getUsername());
			} catch (StoreException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new TenantNotFoundException(tenantName);
		}
		fireTenantManagementListener();
	}

	public Tenant getTenant(User user, User caller) {
		return users.get(user.getUsername());
	}
	
	public IServiceLevelAgreement getSLAForUser(User user){
		Tenant tenant = users.get(user.getUsername());
		if (tenant != null){
			return tenant.getServiceLevelAgreement();
		}
		return null;
	}

	public Collection<Tenant> getTenants() {	
		return Collections.unmodifiableCollection(registeredTenants.values());
	}

}
