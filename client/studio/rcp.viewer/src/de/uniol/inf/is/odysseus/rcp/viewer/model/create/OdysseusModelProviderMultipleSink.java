/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rcp.viewer.model.create;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IConnectionModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.impl.OdysseusConnectionModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.impl.OdysseusGraphModel;
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
	
	private final IOdysseusGraphModel graphModel;
	private Collection<Object> traversedObjects = new ArrayList<Object>();
	
	/**
	 * Konstruktor. Erstellt eine OdysseusModelProviderSink-Instanz.
	 * Ist der Parameter null, so wird eine IllegalArgumentException geworfen.
	 * Innerhalb des Konstruktors wird der physische Ablaufplan eingelesen und in
	 * ein Graphenmodell überführt. Keine zusätzlicher Aufruf notwendig.
	 * 
	 * @param sinks Senken, welche die Ausgangspunkte der Traversierung des Ablaufplans darstellt.
	 */
	public OdysseusModelProviderMultipleSink( List<IPhysicalOperator> sinks ) {
		if( sinks == null ) 
			throw new IllegalArgumentException("sinks is null!");
		
		graphModel = new OdysseusGraphModel();
		
		logger.info( "reading operator-tree from ODYSSEUS" );
		
		for( IPhysicalOperator s : sinks ) 
			parse( s, null, graphModel, null, false);
		
		logger.info( "reading operator-tree finished successfully!" );
	}
	
	
	@Override
	public IGraphModel<IPhysicalOperator> get() {
		return graphModel;
	}
	
	// Verarbeitet eine Senke und dessen Subscriptions
	// Handelt es sich um eine ISource, so werden dessen Subscriptions ebenfalls
	// verarbeitet
	private <T extends IStreamObject<?>> void parse( IPhysicalOperator operator, AbstractPhysicalSubscription<?> fromSub, IGraphModel<IPhysicalOperator> graphModel, INodeModel<IPhysicalOperator> srcNode, boolean back ) {
		
		// Suchen, ob der Objekt schon im Graphen ist.
		// Ist dieser schon im Graphenmodell vorhanden, so wird der
		// gefundene Knoten weiterverwendet.
		INodeModel<IPhysicalOperator> node = null;	
		for( INodeModel<IPhysicalOperator> n : graphModel.getNodes() ) {
			if( n.getContent() == operator ) {
				node = n;
				break;
			}
		}
		// Wurde das Objekt im Graphenmodell nicht gefunden, so muss
		// ein neuer Knoten erzeugt und in das Graphenmodell hinzugefügt werden.
		if( node == null ) {
			node = new OdysseusNodeModel( operator );
			graphModel.addNode( node );
		} 
		
		// Ist ein Quellknoten angegeben, wovon wir gerade kommen, dann bedeutet dies, 
		// dass eine Verbindung vorhanden ist. srcNode ist null, wenn wir an der obersten Senke sind.
		if( srcNode != null && !traversedObjects.contains(fromSub)) {
			if( back == false ) {
				final IConnectionModel<IPhysicalOperator> conn = new OdysseusConnectionModel(node, srcNode);
				graphModel.addConnection( conn );
			} else {
				// Wir gehen Rückwärts...
				final IConnectionModel<IPhysicalOperator> conn = new OdysseusConnectionModel(srcNode, node);
				graphModel.addConnection( conn );
			}
			traversedObjects.add(fromSub);
		}
		
		// Wenn das Objekt zuvor schon besucht wurde, dann dürfen wir
		// den Subscriptions nicht folgen.
		if( traversedObjects.contains(operator)) {
			return;
		} 
		traversedObjects.add( operator );
		
		// Subscriptions folgen
		if( operator.isSink() ) {
			@SuppressWarnings("unchecked")
			ISink<T> sink = (ISink<T>)operator;
			Collection< AbstractPhysicalSubscription< ISource<? extends T> >> sources = sink.getSubscribedToSource();
			for( AbstractPhysicalSubscription< ISource<? extends T> > sub : sources ) {
				if( sub.getTarget().isSink() ) {
					parse( (ISink<?>)sub.getTarget(), sub, graphModel, node, false );
				} else {
					parse( sub.getTarget(), sub, graphModel, node, false );
				}
			}
		}
		
		if( operator.isSource() ) {
			for (AbstractPhysicalSubscription<? extends ISink<?>> sub: ((ISource<?>)operator).getSubscriptions() ){
				parse( (ISink<?>)sub.getTarget(), sub, graphModel, node, true );
			}
		}
	}
	
	// Verarbeitet eine Quelle und dessen Subscriptions
	private <T> void parse( ISource< ? > source, AbstractPhysicalSubscription<?> fromSub, IGraphModel<IPhysicalOperator> graphModel, INodeModel<IPhysicalOperator> srcNode, boolean back) {

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
		if( srcNode != null && !traversedObjects.contains(fromSub)) {
			if( back == false ) {
				final IConnectionModel<IPhysicalOperator> conn = new OdysseusConnectionModel(node, srcNode);
				graphModel.addConnection( conn );
			} else {
				// Wir gehen Rückwärts...
				final IConnectionModel<IPhysicalOperator> conn = new OdysseusConnectionModel(srcNode, node);
				graphModel.addConnection( conn );
			}
			traversedObjects.add(fromSub);
		}
		
		// Wenn das Objekt zuvor schon besucht wurde, dann dürfen wir
		// den Subscriptions nicht folgen.
		if( traversedObjects.contains(source)) {
			return;
		} 
		traversedObjects.add( source );
		
		// Subscriptions folgen
		for (AbstractPhysicalSubscription<? extends ISink<?>> sub: source.getSubscriptions() ){
			parse( (ISink<?>)sub.getTarget(), sub, graphModel, node, true );
		}
	}
}
