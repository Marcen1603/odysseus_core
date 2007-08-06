/*
 * Created on 07.12.2004
 *
 */
package mg.dynaquest.queryexecution.po.algebra;

import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author  Marco Grawunder
 */
public class ProjectPO extends UnaryAlgebraPO{
    
    /**
	 * @uml.property  name="projectAttributes"
	 * @uml.associationEnd  
	 */
    private SDFAttributeList projectAttributes;

    /**
	 * @return  Returns the projectAttributes.
	 * @uml.property  name="projectAttributes"
	 */
    public synchronized SDFAttributeList getProjectAttributes() {
        return projectAttributes;
    }
    /**
	 * @param projectAttributes  The projectAttributes to set.
	 * @uml.property  name="projectAttributes"
	 */
    public synchronized void setProjectAttributes(SDFAttributeList projectAttributes) {
        this.projectAttributes = projectAttributes;
    }
    /**
     * @param projectPO
     */
    public ProjectPO(ProjectPO projectPO) {
        super(projectPO);
        this.projectAttributes = projectPO.projectAttributes;
    }
    
    public ProjectPO() {
    	super();
    	setPOName("ProjectPO");
    }
    
    public ProjectPO(SDFAttributeList queryAttributes) 
    {
    	super();
    	setPOName("ProjectPO");
    	this.projectAttributes = queryAttributes;
    }
    

	public @Override
	SupportsCloneMe cloneMe() {
		return new ProjectPO(this);
	}

}
