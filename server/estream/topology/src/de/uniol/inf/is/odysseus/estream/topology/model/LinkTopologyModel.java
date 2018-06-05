package de.uniol.inf.is.odysseus.estream.topology.model;

/**
 * Data Model for Estream Topology
 * 
 * @author Marcel Hamacher
 *
 */
public class LinkTopologyModel {

	private String source;
	private String target;

	/*
	 * Source, Target
	 */
	public LinkTopologyModel(String source, String target) {
		this.source = source;
		this.target = target;
	}
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getByName(String name) {
        switch(name){
        case "source":
            return source;
        case "target":
            return target;
        default:
            return null;
        }
	}
}
		

