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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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
public final class OdysseusModelProviderMultipleSinkOneWay implements IModelProvider<IPhysicalOperator> {
	
	private static final Logger logger = LoggerFactory.getLogger( OdysseusModelProviderMultipleSinkOneWay.class );
	
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
	public OdysseusModelProviderMultipleSinkOneWay( Collection<IPhysicalOperator> operators ) {
		if( operators == null || operators.isEmpty()) 
			throw new IllegalArgumentException("sinks is null or empty!");
		
		graphModel = new OdysseusGraphModel();
		
		logger.trace( "reading operator-tree from ODYSSEUS" );
		for( IPhysicalOperator operator: operators ) {
			parse( operator, graphModel, null);
		}
		
		logger.trace( "reading operator-tree finished successfully!" );
		
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
	private <T extends IStreamObject<?>> void parse( IPhysicalOperator operator, IGraphModel<IPhysicalOperator> graphModel, INodeModel<IPhysicalOperator> srcNode) {
		
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
		if( srcNode != null ) {
			final IConnectionModel<IPhysicalOperator> conn = new OdysseusConnectionModel(node, srcNode);
			graphModel.addConnection( conn );
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
			Collection< AbstractPhysicalSubscription< ISource<IStreamObject<?>>,?>> sources = sink.getSubscribedToSource();
			for( AbstractPhysicalSubscription<ISource<IStreamObject<?>>,?> sub : sources ) {
				if( sub.getSource().isSink() ) {
					parse( (ISink<?>)sub.getSource(), graphModel, node);
				} else {
					parse( sub.getSource(), graphModel, node);
				}
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
			final IConnectionModel<IPhysicalOperator> conn = new OdysseusConnectionModel(node, srcNode);
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
