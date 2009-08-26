package de.uniol.inf.is.odysseus.logicaloperator.base;

import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;

public class QueryAccessAO extends AccessAO {
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((query == null) ? 0 : query.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		QueryAccessAO other = (QueryAccessAO) obj;
		if (query == null) {
			if (other.query != null)
				return false;
		} else if (!query.equals(other.query))
			return false;
		return true;
	}

	private String query;
	
	public QueryAccessAO() {
		super();
	}
	
	public QueryAccessAO(QueryAccessAO ao) {
		super(ao);
		this.query = ao.query;
	}
	
	public QueryAccessAO(SDFSource source) {
		super(source);
		this.query = source.getStringAttribute("query");
	}
	
	public String getQuery() {
		return this.query;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
}
