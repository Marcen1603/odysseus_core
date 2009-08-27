package de.uniol.inf.is.odysseus.viewer.view.stream;

public interface IDiagramFactory {

	public <In, Out> DiagramConverterPair<In,Out> create( DiagramInfo info );

}
