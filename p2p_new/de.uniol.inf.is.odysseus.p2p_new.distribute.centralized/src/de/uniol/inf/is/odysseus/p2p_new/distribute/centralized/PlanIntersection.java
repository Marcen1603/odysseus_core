package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * A PlanIntersection describes a point in a query-Plan, at which a new Operator uses a result from a running operator.
 * Since the centralized distributor has knowledge of the running plans at the peers and the new queries,
 * it can determine those points beforehand and only send the needed operators. A PlanIntersection is used to convey to the peer,
 * at which point certain sent operators should be weaved into the old plan.
 * The new Operator always uses the old one as a source, never the other way around.
 */
public class PlanIntersection {
	private int oldOperatorID;
	private int newOperatorID;
	private int sinkInPort;
	private int sourceOutPort;
	private SDFSchema schema;
	
	public PlanIntersection(int oldOperatorID, int newOperatorID, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		this.oldOperatorID = oldOperatorID;
		this.newOperatorID = newOperatorID;
		this.sinkInPort = sinkInPort;
		this.sourceOutPort = sourceOutPort;
		this.schema = schema;
	}
	
	public int getOldOperatorID() {
		return oldOperatorID;
	}
	public void setOldOperatorID(int oldOperatorID) {
		this.oldOperatorID = oldOperatorID;
	}
	public int getNewOperatorID() {
		return newOperatorID;
	}
	public void setNewOperatorID(int newOperatorID) {
		this.newOperatorID = newOperatorID;
	}
	public int getSinkInPort() {
		return sinkInPort;
	}
	public void setSinkInPort(int sinkInPort) {
		this.sinkInPort = sinkInPort;
	}
	public int getSourceOutPort() {
		return sourceOutPort;
	}
	public void setSourceOutPort(int sourceOutPort) {
		this.sourceOutPort = sourceOutPort;
	}
	public SDFSchema getSchema() {
		return schema;
	}
	public void setSchema(SDFSchema schema) {
		this.schema = schema;
	}
}
