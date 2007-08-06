/*
 * Created on 07.06.2006
 *
 */
package mg.dynaquest.queryexecution.po.algebra;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFPredicate;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

 
/**
 * @author  Marco Grawunder
 */
public abstract class AlgebraPO implements SupportsCloneMe{
   
    /**
	 * @uml.property  name="poname"
	 */
    private String poname = "<undefined AlgebraOperator>";
    
    private List<AlgebraPO> inputPO = new ArrayList<AlgebraPO>();
    private List<PlanOperator> phyInputPO = new ArrayList<PlanOperator>();
    /**
	 * @uml.property  name="outElements"
	 * @uml.associationEnd  
	 */
    private SDFAttributeList outElements = null;
    /**
	 * @uml.property  name="predicate"
	 * @uml.associationEnd  
	 */
    private SDFPredicate predicate = null;;
    /**
	 * @uml.property  name="sorted"
	 * @uml.associationEnd  
	 */
    private SDFAttributeList sorted = null;;
    /**
	 * @uml.property  name="executionPlace"
	 */
    private URI executionPlace = null;
    
    public AlgebraPO(AlgebraPO po){
        this.inputPO = po.inputPO;
        phyInputPO = po.phyInputPO;
        outElements = po.outElements;
        predicate = po.predicate;
        sorted = po.sorted;
        executionPlace = po.executionPlace;
    }

    public AlgebraPO(){
    	
    }
    
    /**
	 * @return  the outElements
	 * @uml.property  name="outElements"
	 */
    public SDFAttributeList getOutElements() {
        return outElements;
    }

    /**
	 * @param outElements  the outElements to set
	 * @uml.property  name="outElements"
	 */
    public void setOutElements(SDFAttributeList outElements) {
        this.outElements = outElements;        
    }

    /**
	 * @return  the predicate
	 * @uml.property  name="predicate"
	 */
    public SDFPredicate getPredicate() {
        return predicate;
    }

    /**
	 * @param predicate  the predicate to set
	 * @uml.property  name="predicate"
	 */
    protected void setPredicate(SDFPredicate predicate) {
        this.predicate = predicate;
    }

    /**
	 * @return  the sorted
	 * @uml.property  name="sorted"
	 */
    public SDFAttributeList getSorted() {
        return sorted;
    }

    /**
	 * @param sorted  the sorted to set
	 * @uml.property  name="sorted"
	 */
    public void setSorted(SDFAttributeList sorted) {
        this.sorted = sorted;
    }

    /**
	 * @return  the executionPlace
	 * @uml.property  name="executionPlace"
	 */
    public URI getExecutionPlace() {
        return executionPlace;
    }

    /**
	 * @param executionPlace  the executionPlace to set
	 * @uml.property  name="executionPlace"
	 */
    public void setExecutionPlace(URI executionPlace) {
        this.executionPlace = executionPlace;
    }

    public void setInputPO(int pos, AlgebraPO input) {
        this.inputPO.set(pos,input);      
    }

    public void setPhysInputPO(int pos, PlanOperator input) {
        this.phyInputPO.set(pos, input);      
    }

    public void setNoOfInputs(int count){
        this.inputPO = new ArrayList<AlgebraPO>(count);
        this.phyInputPO = new ArrayList<PlanOperator>(count);
        for (int i=0;i<count;i++){
            inputPO.add(null);
            phyInputPO.add(null);
        }
    }
    
    public AlgebraPO getInputPO(int pos) {
        return inputPO.get(pos);
    }    
  
    public PlanOperator getPhysInputPO(int pos) {
        return phyInputPO.get(pos);
    }  
    
    public void replaceInput(SupportsCloneMe oldInput, SupportsCloneMe newInput) {
       throw new NotImplementedException();
    }

    public int getNumberOfInputs() {
        return inputPO.size();
    }


    public String getPOName() {
       return poname;
    }

    public void setPOName(String name){
        this.poname = name;
    }
    
    /* (non-Javadoc)
	 * @see mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe#cloneMe()
	 */
    public abstract SupportsCloneMe cloneMe();

	public void getInternalXMLRepresentation(String baseIndent, String indent, StringBuffer xmlRetValue) {
		xmlRetValue.append(indent).append("<outElements>\n");
		outElements.getXMLRepresentation(indent+indent, xmlRetValue);
		xmlRetValue.append(indent).append("</outElements>\n");
		if (predicate != null){
			xmlRetValue.append(indent).append("<predicate>");
			predicate.getXMLRepresentation(indent, xmlRetValue);
			xmlRetValue.append("</predicate>\n");
		}
		if (sorted != null){
			xmlRetValue.append(indent).append("<sorted>");
			sorted.getXMLRepresentation(indent, xmlRetValue);
			xmlRetValue.append("</sorted>\n");
		}
		if (executionPlace != null){
			xmlRetValue.append(indent).append("<executionPlace>").append(executionPlace).append("</executionPlace>\n");
		}
	}
}
