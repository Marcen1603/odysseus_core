package de.uniol.inf.is.odysseus.viewer.view.stream;

public final class DiagramInfo {

	private String diagramType;
	private String converterType;
	private Parameters params;
	private String name;
	
	public DiagramInfo( String name, String diagramType, String converterType, Parameters params ) {
		this.diagramType = diagramType;
		this.converterType = converterType;
		this.name = name;
		this.params = params;
	}
	
	public String getDiagramType() {
		return diagramType;
	}
	
	public String getConverterType() {
		return converterType;
	}
	
	public Parameters getParameters() {
		return params;
	}
	
	public String getName() {
		return name;
	}
}
