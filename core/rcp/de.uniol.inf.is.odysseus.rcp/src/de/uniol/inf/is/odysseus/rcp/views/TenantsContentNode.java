package de.uniol.inf.is.odysseus.rcp.views;

import java.util.Collection;

import de.uniol.inf.is.odysseus.usermanagement.Tenant;

class TenantsContentNode {
	public Collection<Tenant> tenants;

	public TenantsContentNode(Collection<Tenant> tenants) {
		this.tenants = tenants;
	}
}
