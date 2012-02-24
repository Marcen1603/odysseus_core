package de.uniol.inf.is.odysseus.core.server.sparql.physicalops.base.project;

import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.IClone;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.operators.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.object.NodeList;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.util.SPARQL_Util;
import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFSchema;

public class NodeListProjectPO<T extends IClone> extends AbstractPipe<NodeList<T>, NodeList<T>> {

	ProjectAO ao;
	boolean enforceOutputSchema;
	int[] restrictList;
	
	public NodeListProjectPO(ProjectAO projectAO, boolean enforceOutputSchema) {
		this.ao = projectAO;
		this.enforceOutputSchema = enforceOutputSchema;
		this.initRestrictList();
	}

	public NodeListProjectPO(NodeListProjectPO<T> projectPO) {
		this.ao = projectPO.ao;
		this.enforceOutputSchema = projectPO.enforceOutputSchema;
		this.restrictList = projectPO.restrictList;
		// sollte funktionieren, ohne vorher noch mal explizit den physical input po zu setzen
		this.initRestrictList();
	}

	public NodeListProjectPO<T> clone() {
		return new NodeListProjectPO<T>(this);
	}

	
	protected void initRestrictList(){
		SDFSchema out = ao.getOutputSchema();
		SDFSchema in = ao.getInputSchema();
		System.out.print("Project in: ");
		for(SDFAttribute a: in){
			System.out.print(a.getQualName() + "; ");
		}
		System.out.println();
		System.out.print("Project Out: ");
		for(SDFAttribute a: out){
			System.out.print(a.getQualName() + "; ");
		}
		System.out.println();
		
        restrictList = new int[out.size()];
        for(int j = 0; j < out.size(); ++j) {
        	SDFAttribute curOut = out.get(j);
        	boolean found = false;
        	for (int i = 0; i < in.size(); ++i) {
        		if (in.get(i).getQualName().equals(curOut.getQualName())) {
        			restrictList[j] = i;
        			found = true;
        			break;
        		}
        	}
        	
        	// the variable is extra, so mark this attribute to
        	// add an unbound node
        	if(!found){
        		restrictList[j] = -1;
        	}
        }
	}
	
	public NodeList<T> project(NodeList<T> element) {
		NodeList<T> retVal = new NodeList<T>();
		retVal.setMetadata(element.getMetadata());
			
		for (int i: restrictList){
			if(i < 0){
				retVal.add(SPARQL_Util.UNBOUND_ATTRIBUTE_NODE);
			}
			// if an extra attribute occurs,
			// this will be handled as unbound attribute
			// in the query result
			else if(element.get(i) == SPARQL_Util.EXTRA_ATTRIBUTE_NODE){
				retVal.add(SPARQL_Util.UNBOUND_ATTRIBUTE_NODE);
			}
			else{
				retVal.add(element.get(i));
			}
		}
		return retVal;
	}
		
	public void process_close(){
	}
	
	public void process_next(NodeList<T> object, int port, boolean isReadOnly){
		if(isReadOnly){
			object = object.clone();
		}
		
		this.transfer(this.project(object));
	}
}
