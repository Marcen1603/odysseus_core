package de.uniol.inf.is.odysseus.logicaloperator.base;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;

public class AccessAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 6119364096911629084L;

	/**
	 * Die Uri der von diesem AccessPO gekapselten Quelle
	 */
	protected SDFSource source = null;

	private int port;

	private String host;
	
	/**
	 * This variable will be used to generate an ID for every new input tuple
	 */
	private static long ID = 1;
	
	/**
	 * this variable will be used, if a wildcard is necessary for an id
	 */
	private static Long wildcard = new Long(-1);

	public AccessAO(AbstractLogicalOperator po) {
		super(po);
	}

	public AccessAO() {
		super();
	}
	
	public AccessAO(AccessAO po){
		super(po);
		this.source = po.source;
	}
	
	public AccessAO(SDFSource source) {
		this.source = source;
	}

	/**
	 * @return the source
	 */
	public synchronized SDFSource getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public synchronized void setSource(SDFSource source) {
		this.source = source;
	}

	@Override
	public AccessAO clone() {
		return new AccessAO(this);
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}
	
	public String getSourceType() {
		return this.source.getSourceType();
	}
	
	private static long genID(){
		return ++ID;
	}
	
	public static List<Long> nextID(){
		ArrayList<Long> idList = new ArrayList<Long>();
		idList.add(new Long(genID()));
		return idList;
	}
	
	public static Long getWildcard(){
		return wildcard;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
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
		AccessAO other = (AccessAO) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (port != other.port)
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}

	public void setPort(int port) {
		this.port = port;		
	}
	
	public int getPort() {
		return port;
	}

	public void setHost(String host) {
		this.host = host;
	}
	
	public String getHost() {
		return host;
	}
	
	@Override
	public String toString() {
		return getName() + " (" + this.getSource().getURI() + ")";
	}
	
}
