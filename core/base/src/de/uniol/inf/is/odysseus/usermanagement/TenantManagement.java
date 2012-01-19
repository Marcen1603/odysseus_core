/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
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

	public void createTenant(String name, String slaName, ISession caller) {
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

	public void addUserToTenant(Tenant tenant, IUser user, ISession caller)
			throws TenantNotFoundException, TooManyUsersException {
		addUserToTenant(tenant.getName(), user, caller);
	}

	public void addUserToTenant(String tenantName, IUser user, ISession caller)
			throws TenantNotFoundException, TooManyUsersException {
		Tenant t = registeredTenants.get(tenantName);
		if (t != null) {
			t.addUser(user);
			try {
				users.put(user.getName(), t);
			} catch (StoreException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new TenantNotFoundException(tenantName);
		}
		fireTenantManagementListener();
	}

	public void removeUserFromTenant(Tenant tenant, IUser user, ISession caller)
			throws TenantNotFoundException {
		removeUserFromTenant(tenant.getName(), user, caller);
	}

	public void removeUserFromTenant(String tenantName, IUser user, ISession caller)
			throws TenantNotFoundException {
		Tenant t = registeredTenants.get(tenantName);
		if (t != null) {
			t.removeUser(user);
			try {
				users.remove(user.getName());
			} catch (StoreException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new TenantNotFoundException(tenantName);
		}
		fireTenantManagementListener();
	}

	public Tenant getTenant(ISession user, ISession caller) {
		return users.get(user.getUser().getName());
	}
	
	public IServiceLevelAgreement getSLAForUser(ISession user){
		Tenant tenant = users.get(user.getUser().getName());
		if (tenant != null){
			return tenant.getServiceLevelAgreement();
		}
		return null;
	}

	public Collection<Tenant> getTenants() {	
		return Collections.unmodifiableCollection(registeredTenants.values());
	}

}
