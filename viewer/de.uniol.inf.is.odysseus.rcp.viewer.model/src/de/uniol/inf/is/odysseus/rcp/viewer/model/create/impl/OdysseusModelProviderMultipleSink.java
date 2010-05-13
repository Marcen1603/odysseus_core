package de.uniol.inf.is.odysseus.rcp.viewer.model.create.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.IModelProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IConnectionModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.impl.DefaultConnectionModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.impl.DefaultGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.impl.OdysseusNodeModel;

/**
 * Klasse, welcher ausgehend von mehreren gegebenen Senken den physischen Ablaufplan 
 * durchläuft und das Graphenmodell mittels den DefaultImplementierungen aufbaut.
 * 
 * Der Ablaufplan wird von der gegebenen Senke aus durchlaufen. D.h. die Subscriptions
 * der Senken werden betrachtet und verfolgt. Handelt es sich bei einem Knoten im Plan
 * um eine Senke, so werden die Subscriptions weiter verfolgt. Ist es zusätzlich eine ISource,
 * oder nur eine ISource, so werden zur Überürüfung die angemeldeten Senken zurückverfolgt.
 * Dadurch wird jeder zusammenhängende Ablaufplan komplett erfasst.
 * 
 * @author Timo Michelsen
 *
 */
public final class OdysseusModelProviderMultipleSink implements IModelProvider<IPhysicalOperator> {
	
	private static final Logger logger = LoggerFactory.getLogger( OdysseusModelProviderMultipleSink.class );
	
	private final IGraphModel<IPhysicalOperator> graphModel;
	private Collection<Object> traversedObjects = new ArrayList<Object>();
	
	/**
	 * Konstruktor. Erstellt eine OdysseusModelProviderSink-Instanz.
	 * Ist der Parameter null, so wird eine IllegalArgumentException geworfen.
	 * Innerhalb des Konstruktors wird der physische Ablaufplan eingelesen und in
	 * ein Graphenmodell überführt. Keine zusätzlicher Aufruf notwendig.
	 * 
	 * @param sinks Senken, welche die Ausgangspunkte der Traversierung des Ablaufplans darstellt.
	 */
	public OdysseusModelProviderMultipleSink( List<ISink<?>> sinks ) {
		if( sinks == null ) 
			throw new IllegalArgumentException("sinks is null!");
		
		graphModel = new DefaultGraphModel<IPhysicalOperator>();
		
		logger.info( "reading operator-tree from ODYSSEUS" );
		
		for( ISink<?> s : sinks ) 
			parse( s, graphModel, null, false);
		
		logger.info( "reading operator-tree finished successfully!" );
	}
	
	
	@Override
	public IGraphModel<IPhysicalOperator> get() {
		return graphModel;
	}
	
	// Verarbeitet eine Senke und dessen Subscriptions
	// Handelt es sich um eine ISource, so werden dessen Subscriptions ebenfalls
	// verarbeitet
	private <T> void parse( ISink<T> sink, IGraphModel<IPhysicalOperator> graphModel, INodeModel<IPhysicalOperator> srcNode, boolean back ) {
		
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
			if( back == false ) {
				final IConnectionModel<IPhysicalOperator> conn = new DefaultConnectionModel<IPhysicalOperator>(node, srcNode);
				graphModel.addConnection( conn );
			} else {
				// Wir gehen Rückwärts...
				final IConnectionModel<IPhysicalOperator> conn = new DefaultConnectionModel<IPhysicalOperator>(srcNode, node);
				graphModel.addConnection( conn );
			}
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
				parse( (ISink<?>)sub.getTarget(), graphModel, node, false );
			} else {
				parse( sub.getTarget(), graphModel, node, false );
			}
		}
		
		if( sink instanceof ISource<?> ) {
			for (PhysicalSubscription<? extends ISink<?>> sub: ((ISource<?>)sink).getSubscriptions() ){
				parse( (ISink<?>)sub.getTarget(), graphModel, node, true );
			}
		}
	}
	
	// Verarbeitet eine Quelle und dessen Subscriptions
	private <T> void parse( ISource< ? > source, IGraphModel<IPhysicalOperator> graphModel, INodeModel<IPhysicalOperator> srcNode, boolean back) {

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
			if( back == false ) {
				final IConnectionModel<IPhysicalOperator> conn = new DefaultConnectionModel<IPhysicalOperator>(node, srcNode);
				graphModel.addConnection( conn );
			} else {
				// Wir gehen Rückwärts...
				final IConnectionModel<IPhysicalOperator> conn = new DefaultConnectionModel<IPhysicalOperator>(srcNode, node);
				graphModel.addConnection( conn );
			}
		}
		
		// Wenn das Objekt zuvor schon besucht wurde, dann dürfen wir
		// den Subscriptions nicht folgen.
		if( traversedObjects.contains(source)) {
			return;
		} 
		traversedObjects.add( source );
		
		// Subscriptions folgen
		for (PhysicalSubscription<? extends ISink<?>> sub: source.getSubscriptions() ){
			parse( (ISink<?>)sub.getTarget(), graphModel, node, true );
		}
	}
}
