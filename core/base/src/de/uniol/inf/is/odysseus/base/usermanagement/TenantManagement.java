package de.uniol.inf.is.odysseus.base.usermanagement;

import java.util.HashMap;
import java.util.Map;

public class TenantManagement {
	static private TenantManagement instance = null;

	public synchronized static TenantManagement getInstance() {
		if (instance == null) {
			instance = new TenantManagement();
		}
		return instance;
	}
	
	private Map<String, Tenant> registeredTenants = new HashMap<String, Tenant>();
	private Map<User, Tenant> users = new HashMap<User, Tenant>();
	
	public void createTenant(String name, IServiceLevelAgreement sla){
		Tenant newT = new Tenant(name, sla);
		registeredTenants.put(name, newT);
	}
	
	public void addUserToTenant(Tenant tenant, User user) throws TenantNotFoundException, TooManyUsersException{
		addUserToTenant(tenant.getName(), user);
	}
		
	public void addUserToTenant(String tenantName, User user) throws TenantNotFoundException, TooManyUsersException{
		Tenant t = registeredTenants.get(tenantName);
		if (t != null){
			t.addUser(user);
			users.put(user, t);
		}else{
			throw new TenantNotFoundException(tenantName);
		}
	}
	
	public void removeUserFromTenant(Tenant tenant, User user) throws TenantNotFoundException{
		removeUserFromTenant(tenant.getName(), user);
	}

	
	public void removeUserFromTenant(String tenantName, User user) throws TenantNotFoundException{
		Tenant t = registeredTenants.get(tenantName);
		if (t != null){
			t.removeUser(user);
			users.remove(user);
		}else{
			throw new TenantNotFoundException(tenantName);
		}		
	}
	
	public Tenant getTenant(User user){
		return users.get(user);
	}

}
