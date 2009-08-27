package de.uniol.inf.is.odysseus.viewer.view.stream;



public class DiagramConverterPair<In,Out> {

	private IStreamDiagram<Out> diagram;
	private IStreamElementConverter<In,Out> converter;
	
	public DiagramConverterPair( IStreamElementConverter<In,Out> converter, IStreamDiagram<Out> diagram) {
		this.diagram = diagram;
		this.converter = converter;
	}
	
	public IStreamDiagram<Out> getDiagram() {
		return diagram;
	}
	
	public IStreamElementConverter<In,Out> getConverter() {
		return converter;
	}
}
