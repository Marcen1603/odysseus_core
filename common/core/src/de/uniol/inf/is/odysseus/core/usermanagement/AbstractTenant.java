package de.uniol.inf.is.odysseus.core.usermanagement;

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractTenant other = (AbstractTenant) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
