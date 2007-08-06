package mg.dynaquest.queryexecution.po.algebra;

import mg.dynaquest.sourcedescription.sdf.description.SDFSource;

public class AccessPO extends AlgebraPO {

	/**
	 * Die Uri der von diesem AccessPO gekapselten Quelle
	 * 
	 * @uml.property name="source"
	 * @uml.associationEnd
	 */
	protected SDFSource source = null;

	public AccessPO(AlgebraPO po) {
		super(po);
	}

	public AccessPO() {
		super();
	}
	
	public AccessPO(AccessPO po){
		super(po);
		this.source = po.source;
	}
	
	public AccessPO(SDFSource source) {
		this.source = source;
	}

	/**
	 * @return the source
	 * @uml.property name="source"
	 */
	public synchronized SDFSource getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 * @uml.property name="source"
	 */
	public synchronized void setSource(SDFSource source) {
		this.source = source;
	}

	@Override
	public SupportsCloneMe cloneMe() {
		return new AccessPO(this);
	}

	@Override
	public String getPOName() {
		return this.getClass().getSimpleName();
	}
}