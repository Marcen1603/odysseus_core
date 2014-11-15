package de.uniol.inf.is.odysseus.sports.rest.dto;

import java.util.ArrayList;

/**
 * This class represents a data transfer object for the metadata
 * @author Thomas
 *
 */
public class MetadataInfo {

	private ArrayList<ElementInfo> elements;
	private ArrayList<GoalInfo> goals;
	private FieldInfo field;
	
	public MetadataInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public MetadataInfo(ArrayList<ElementInfo> elements, ArrayList<GoalInfo> goals, FieldInfo field) {
		this.elements = elements;
		this.goals = goals;
		this.field = field;
	}

	public ArrayList<ElementInfo> getElements() {
		return elements;
	}

	public void setElements(ArrayList<ElementInfo> elements) {
		this.elements = elements;
	}

	public ArrayList<GoalInfo> getGoals() {
		return goals;
	}

	public void setGoals(ArrayList<GoalInfo> goals) {
		this.goals = goals;
	}

	public FieldInfo getField() {
		return field;
	}

	public void setField(FieldInfo field) {
		this.field = field;
	}
}
