package de.uniol.inf.is.odysseus.estream.topology.model;

/**
 * Data Model for Estream Topology
 * 
 * @author Marcel Hamacher
 *
 */
public class TopologyData {

	private String sm_id;
	private String model;
	private String smgw_id;
	private String node;
	private String transformator;
	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/*
	 * Smartmeter ID, Model-Name, Gateway ID, Node-ID, Transformator-ID, Type
	 */
	public TopologyData(String sm_id, String model, String smgw_id, String node, String transformator, String type) {
		this.sm_id = sm_id;
		this.model = model;
		this.smgw_id = smgw_id;
		this.node = node;
		this.transformator = transformator;
		this.type = type;
	}
	
	public String getSm_id() {
		return sm_id;
	}
	public void setSm_id(String sm_id) {
		this.sm_id = sm_id;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSmgw_id() {
		return smgw_id;
	}
	public void setSmgw_id(String smgw_id) {
		this.smgw_id = smgw_id;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getTransformator() {
		return transformator;
	}
	public void setTransformator(String transformator) {
		this.transformator = transformator;
	}	
}
		

