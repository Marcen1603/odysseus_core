package de.uniol.inf.is.odysseus.core.server.usermanagement;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.usermanagement.IAbstractEntity;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;

abstract public class AbstractTenant implements ITenant, Serializable {
	
	private static final long serialVersionUID = 2607833722589527621L;
	private String name;

	@Override
	public void update(IAbstractEntity entity) {
		if (entity instanceof AbstractTenant){
			this.name = ((AbstractTenant) entity).name;
		}
	}

	@Override
	public int compareTo(ITenant o) {
		return this.getName().compareTo(o.getName());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

}
