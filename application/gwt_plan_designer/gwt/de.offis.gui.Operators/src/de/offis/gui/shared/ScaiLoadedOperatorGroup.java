package de.offis.gui.shared;

import java.io.Serializable;
import java.util.ArrayList;

import de.offis.client.ScaiLink;
import de.offis.gui.client.module.AbstractModuleModel;

/**
 * Model for a loaded operator group.
 *
 * @author Alexander Funk
 * 
 */
public class ScaiLoadedOperatorGroup implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 260643724135472532L;
	private ArrayList<AbstractModuleModel> models;
	private ArrayList<ScaiLink> links;
	
	protected ScaiLoadedOperatorGroup(){
		
	}
	
	public ScaiLoadedOperatorGroup(ArrayList<AbstractModuleModel> models, ArrayList<ScaiLink> links) {
		this.models = models;
		this.links = links;
	}
	
	public ArrayList<ScaiLink> getLinks() {
		return links;
	}
	
	public ArrayList<AbstractModuleModel> getModels() {
		return models;
	}
}
