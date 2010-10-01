/*
 * Created on 10.12.2004
 *
 */
package de.uniol.inf.is.odysseus.logicaloperator;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Marco Grawunder
 *
 */
public class SortAO extends UnaryLogicalOp{

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(ascending);
		result = prime * result
				+ ((sortAttribs == null) ? 0 : sortAttribs.hashCode());
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
		SortAO other = (SortAO) obj;
		if (!Arrays.equals(ascending, other.ascending))
			return false;
		if (sortAttribs == null) {
			if (other.sortAttribs != null)
				return false;
		} else if (!sortAttribs.equals(other.sortAttribs))
			return false;
		return true;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3251466434501011289L;


	/**
	 * @uml.property  name="sortAttrib"
	 */
    SDFAttributeList sortAttribs = null;
    

    /**
     * tells you if the attribute at the
     * specified index has to be in ascending
     * (true) or descending (false) order
     */
    private boolean[] ascending;


    /**
     * 
     */
    public SortAO(SDFAttributeList set) {
        super();
        this.sortAttribs = set;
        setName("SortPO");
    }

    /**
     * @param sortPO
     */
    public SortAO(SortAO sortPO) {
        super(sortPO);
        this.sortAttribs = sortPO.sortAttribs;
        setName("SortAO");
    }

    /**
     * 
     */
    public SortAO() {
        super();
        setName("SortAO");
    }


    /**
     * @return Returns the sortAttrib.
     * 
     * @uml.property name="sortAttrib"
     */
    public synchronized SDFAttributeList getSortAttrib() {
        return sortAttribs;
    }

    /**
     * @param sortAttrib The sortAttrib to set.
     * 
     * @uml.property name="sortAttrib"
     */
    public synchronized void setSortAttrib(SDFAttributeList sortAttribs) {
        this.sortAttribs = sortAttribs;
    }

	public @Override
	SortAO clone() {
		return new SortAO(this);
	}
	
	public boolean[] getAscending() {
		return ascending;
	}

	public void setAscending(boolean[] ascending) {
		this.ascending = ascending;
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema();
	}
	

}
