/*
 * Created on 10.12.2004
 *
 */
package mg.dynaquest.queryexecution.po.algebra;

import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Marco Grawunder
 *
 */
public class SortPO extends UnaryAlgebraPO{

    /**
	 * @uml.property  name="sortAttrib"
	 */
    SDFAttributeList sortAttribs = null;


    /**
     * 
     */
    public SortPO(SDFAttributeList set) {
        this.sortAttribs = set;
        setPOName("SortPO");
    }

    /**
     * @param sortPO
     */
    public SortPO(SortPO sortPO) {
        super(sortPO);
        this.sortAttribs = sortPO.sortAttribs;
        setPOName("SortPO");
    }

    /**
     * 
     */
    public SortPO() {
        super();
        setPOName("SortPO");
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
	SupportsCloneMe cloneMe() {
		return new SortPO(this);
	}

}
