package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer;

public class LayerConfiguration {
	
	private String title;
	private String type;
	private boolean visibility;

	public LayerConfiguration(){
		
	}
	

	public LayerConfiguration(boolean visibility, String title, String type){
		this.visibility = visibility;
		this.title  = title;
		this.type = type;

	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getType(){
		return this.type;
	}
	
	public boolean getVisibility(){
		return this.visibility;
	}
	
	public void setVisibility(boolean checked){
		this.visibility = checked;
	}

}
