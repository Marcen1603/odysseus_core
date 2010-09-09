package de.uniol.inf.is.odysseus.rcp.viewer.model.create;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IConnectionModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.impl.DefaultConnectionModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.impl.OdysseusGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.impl.OdysseusNodeModel;

/**
 * Klasse, welcher ausgehend von einer gegebenen Senke den physischen Ablaufplan 
 * durchläuft und das Graphenmodell mittels den DefaultImplementierungen aufbaut.
 * 
 * Der Ablaufplan wird von der gegebenen Senke aus durchlaufen. D.h. die Subscriptions
 * der Senken werden betrachtet und verfolgt. Handelt es sich bei einem Knoten im Plan
 * um eine Senke, so werden die Subscriptions weiter verfolgt. Dabei wird nur in eine Richtung
 * erfasst. Der Baum wird nicht in Richtung Senke verfolgt.
 * 
 * @author Timo Michelsen
 *
 */
public final class OdysseusModelProviderSinkOneWay implements IModelProvider<IPhysicalOperator> {
	
	private static final Logger logger = LoggerFactory.getLogger( OdysseusModelProviderSinkOneWay.class );
	
	private final IOdysseusGraphModel graphModel;
	private Collection<Object> traversedObjects = new ArrayList<Object>();
	
	/**
	 * Konstruktor. Erstellt eine OdysseusModelProviderSink-Instanz.
	 * Ist der Parameter null, so wird eine IllegalArgumentException geworfen.
	 * Innerhalb des Konstruktors wird der physische Ablaufplan eingelesen und in
	 * ein Graphenmodell überführt. Keine zusätzlicher Aufruf notwendig.
	 * 
	 * @param sink Senke, welcher den Ausgangspunkt der Traversierung des Ablaufplans darstellt.
	 */
	public OdysseusModelProviderSinkOneWay( ISink<?> sink, IQuery query ) {
		if( sink == null ) 
			throw new IllegalArgumentException("sink is null!");
		
		graphModel = new OdysseusGraphModel(query);
		
		logger.info( "reading operator-tree from ODYSSEUS" );
		parse( sink, graphModel, null);
		logger.info( "reading operator-tree finished successfully!" );
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = sdf.format(new Date());
		graphModel.setName("Graph [" + time + "]");
	}
	
	
	@Override
	public IGraphModel<IPhysicalOperator> get() {
		return graphModel;
	}
	
	// Verarbeitet eine Senke und dessen Subscriptions
	// Handelt es sich um eine ISource, so werden dessen Subscriptions ebenfalls
	// verarbeitet
	private <T> void parse( ISink<T> sink, IGraphModel<IPhysicalOperator> graphModel, INodeModel<IPhysicalOperator> srcNode) {
		
		// Suchen, ob der Objekt schon im Graphen ist.
		// Ist dieser schon im Graphenmodell vorhanden, so wird der
		// gefundene Knoten weiterverwendet.
		INodeModel<IPhysicalOperator> node = null;	
		for( INodeModel<IPhysicalOperator> n : graphModel.getNodes() ) {
			if( n.getContent() == sink ) {
				node = n;
				break;
			}
		}
		// Wurde das Objekt im Graphenmodell nicht gefunden, so muss
		// ein neuer Knoten erzeugt und in das Graphenmodell hinzugefügt werden.
		if( node == null ) {
			node = new OdysseusNodeModel( sink );
			graphModel.addNode( node );
		} 
		
		// Ist ein Quellknoten angegeben, wovon wir gerade kommen, dann bedeutet dies, 
		// dass eine Verbindung vorhanden ist. srcNode ist null, wenn wir an der obersten Senke sind.
		if( srcNode != null ) {
			final IConnectionModel<IPhysicalOperator> conn = new DefaultConnectionModel<IPhysicalOperator>(node, srcNode);
			graphModel.addConnection( conn );
		}
		
		// Wenn das Objekt zuvor schon besucht wurde, dann dürfen wir
		// den Subscriptions nicht folgen.
		if( traversedObjects.contains(sink)) {
			return;
		} 
		traversedObjects.add( sink );
		
		// Subscriptions folgen
		Collection< PhysicalSubscription< ISource<? extends T> >> sources = sink.getSubscribedToSource();
		for( PhysicalSubscription< ISource<? extends T> > sub : sources ) {
			if( sub.getTarget() instanceof ISink<?> ) {
				parse( (ISink<?>)sub.getTarget(), graphModel, node);
			} else {
				parse( sub.getTarget(), graphModel, node);
			}
		}
	}
	
	// Verarbeitet eine Quelle und dessen Subscriptions
	private <T> void parse( ISource< ? > source, IGraphModel<IPhysicalOperator> graphModel, INodeModel<IPhysicalOperator> srcNode) {

		// Suchen, ob der Objekt schon im Graphen ist.
		// Ist dieser schon im Graphenmodell vorhanden, so wird der
		// gefundene Knoten weiterverwendet.
		INodeModel<IPhysicalOperator> node = null;	
		for( INodeModel<IPhysicalOperator> n : graphModel.getNodes() ) {
			if( n.getContent() == source ) {
				node =  n;
				break;
			}
		}
		// Wurde das Objekt im Graphenmodell nicht gefunden, so muss
		// ein neuer Knoten erzeugt und in das Graphenmodell hinzugefügt werden.
		if( node == null ) {
			node = new OdysseusNodeModel( source );
			graphModel.addNode( node );
		} 
		
		// Ist ein Quellknoten angegeben, wovon wir gerade kommen, dann bedeutet dies, 
		// dass eine Verbindung vorhanden ist. srcNode ist null, wenn wir an der obersten Senke sind.
		if( srcNode != null ) {
			final IConnectionModel<IPhysicalOperator> conn = new DefaultConnectionModel<IPhysicalOperator>(node, srcNode);
			graphModel.addConnection( conn );
		}
		
		// Wenn das Objekt zuvor schon besucht wurde, dann dürfen wir
		// den Subscriptions nicht folgen.
		if( traversedObjects.contains(source)) {
			return;
		} 
		traversedObjects.add( source );
	}
}
