package de.uniol.inf.is.odysseus.viewer.swt.diagram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.viewer.view.stream.AbstractStreamConverter;
import de.uniol.inf.is.odysseus.viewer.view.stream.DiagramConverterPair;
import de.uniol.inf.is.odysseus.viewer.view.stream.DiagramInfo;
import de.uniol.inf.is.odysseus.viewer.view.stream.IDiagramFactory;
import de.uniol.inf.is.odysseus.viewer.view.stream.IStreamDiagram;
import de.uniol.inf.is.odysseus.viewer.view.stream.IStreamElementConverter;
import de.uniol.inf.is.odysseus.viewer.view.stream.Parameters;
import de.uniol.inf.is.odysseus.viewer.view.stream.StreamFloatConverter;
import de.uniol.inf.is.odysseus.viewer.view.stream.StreamIntegerConverter;
import de.uniol.inf.is.odysseus.viewer.view.stream.StreamTupleConverter;

public class SWTDiagramFactory implements IDiagramFactory {

	private static final Logger logger = LoggerFactory.getLogger( SWTDiagramFactory.class );
	
	private final Composite composite;
	
	public SWTDiagramFactory( Composite composite ) {
		this.composite = composite;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <In,Out> DiagramConverterPair< In, Out > create( INodeModel<?> node, DiagramInfo info ) {
		if( info == null )
			throw new IllegalArgumentException("info is null");
			
		IStreamDiagram<?> dia = createDiagram( node, info.getDiagramType(), info.getParameters() );
		IStreamElementConverter<?,?> con = createConverter( info.getConverterType(), info.getParameters() );
		
		return new DiagramConverterPair( con, dia );
	}
	
	private AbstractStreamConverter<?,?> createConverter( String type, Parameters params ) {
		if( "default".equals( type ))
			return new StreamTupleConverter( params );
		if( "integer".equals( type ))
			return new StreamIntegerConverter(params);
		if( "float".equals( type ))
			return new StreamFloatConverter(params);
		
		logger.warn("Convertertype " + type + " not found");
		return null;
	}
	
	private AbstractSWTDiagram< ? > createDiagram( INodeModel<?> node, String type, Parameters params ) {
		if( "list".equals(type))
			return new SWTListDiagram(node, composite, params);
		if( "histogram".equals( type ))
			return new SWTHistogram(node, composite, params);
		if( "line".equals( type ))
			return new SWTLineDiagram(node, composite, params );
		
		logger.warn( "Diagramtype " + type + " not found" );
		return null;
	}

	
}
