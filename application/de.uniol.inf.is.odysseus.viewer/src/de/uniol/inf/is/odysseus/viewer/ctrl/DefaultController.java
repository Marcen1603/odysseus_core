package de.uniol.inf.is.odysseus.viewer.ctrl;

import de.uniol.inf.is.odysseus.viewer.model.create.IModelManager;
import de.uniol.inf.is.odysseus.viewer.model.create.ModelManager;

/**
 * Standardimplementierung der IController-Schnittstelle. Wenn der Controller
 * keine speziellen Implementierungen benötigt, kann diese Klasse verwendet 
 * werden.
 * 
 * @author Timo Michelsen
 *
 * @param <C> Typ der Oberklasse aller Operatoren, Senken und Quellen des physischen Ablaufgraphen.
 */
public class DefaultController<C> implements IController<C> {

//	private static final Logger logger = LoggerFactory.getLogger( DefaultController.class );
	
	private ModelManager<C> modelManager = new ModelManager<C>();

	@Override
	public IModelManager<C> getModelManager() {
		return modelManager;
	}
	
//	/**
//	 * Standardkonstruktor. Erstellt eine neue DefaultController-Instanz. Zu Beginn
//	 * ist kein ModelProvider zugewiesen. Das muss mittels setModelProvider() 
//	 * gesondert geschehen.
//	 */
//	public DefaultController() {
//		((Log4JLogger)logger).getLogger().setLevel( Level.DEBUG );
//	}
//	
//	@Override
//	public List<IGraphModel<C>> getModels() {
//		return modelManager.getModels();
//	}
//
//	@Override
//	public void refreshModel() {
//		logger.debug( "Modell wird aktualisiert" );
//		
//		if( modelProvider == null )
//			throw new IllegalArgumentException("modelProvider is null!");
//			
//		final IGraphModel<C> newGraph = modelProvider.get();
//		applyNewGraph( newGraph );
//		
//		logger.debug( "Modell aktualisiert" );
//	}
//	
//	private void applyNewGraph( IGraphModel<C> newGraphModel ) {
//		if( newGraphModel == null )
//			throw new IllegalArgumentException("newGraph is null!");
//			
//		// Ist zuvor kein GraphModel vorhanden, so muss nichts aktualisiert werden.
//		// Der übergebene GraphModel ist vollständig das neue GraphModel des Controllers.
//		if( graphModel == null ) {
//			graphModel = newGraphModel;
//			logger.debug( "Komplett neuen GraphModell erzeugt" );
//			return;
//		}
//		
//		// Wird dieser Punkt erreicht, so existierte bereits ein GraphModel.
//		// Das alte GraphModel wird nun dem neuen GraphModel angepasst. Dabei
//		// ist die Reihenfolge wichtig, erst die Knoten zu betrachten anschließend
//		// die Verbindungen.
//		
//		// Schauen, ob alte Knoten entfernt werden müssen. Dabei wird geschaut,
//		// ob im alten GraphModel Knoten vorhanden sind, die im neuen nicht 
//		// mehr auftauchen. Diese müssen dann entfernt werden.
//		for( INodeModel<C> node : graphModel.getNodes().toArray( new INodeModel[0] ) ) {
//			INodeModel<C> foundNode = findNodeWithContent( newGraphModel, node.getContent());
//			if( foundNode == null ) {
//				// Knoten ist im neuen Graphen nicht vorhanden
//				logger.debug( "Entferne alten Knoten " + node );
//				graphModel.removeNode( node );
//			}
//		}
//		
//		// Schauen, ob neue Knoten hinzugefügt werden müssen. Dabei wird geschaut,
//		// ob im neuen GraphModel Knoten vorhanden sind, welche im alten nicht
//		// auftauchen. Diese werden dann dem alten hinzugefügt. Verbindungen
//		// zwischen neuen und alten Knoten werden bewusst erst später betrachtet.
//		for( INodeModel<C> node : newGraphModel.getNodes() ) {
//			INodeModel<C> foundNode = findNodeWithContent( graphModel, node.getContent() );
//			if( foundNode == null ) {
//				logger.debug( "Füge neuen Knoten hinzu: " + node );
//				graphModel.addNode( node );
//			}
//		}
//		
//		// Schauen, ob Verbindungen entfernt werden müssen. Es können Verbindungen
//		// im alten GraphModel vorhanden sein, die im neuen nicht existieren. Diese
//		// müssen dann entfernt werden.
//		for( IConnectionModel<C> conn : graphModel.getConnections() ) {
//			IConnectionModel<C> foundConn = findConnectionWithContents( newGraphModel, conn.getStartNode().getContent(), conn.getEndNode().getContent());
//			if( foundConn == null ) {
//				logger.debug( "Entferne alte Verbindung " + conn );
//				graphModel.removeConnection( conn );
//			}
//		}
//		
//		// Schauen, ob neue Verbindungen hinzugefügt werden müssen. Analog
//		// zum Knoten hinzufügen.
//		for( IConnectionModel<C> conn : newGraphModel.getConnections() ) {
//			IConnectionModel<C> foundConn = findConnectionWithContents( graphModel, conn.getStartNode().getContent(), conn.getEndNode().getContent());
//			if( foundConn == null ) {
//				logger.debug( "Füge neue Verbindung hinzu: " + conn );
//				graphModel.addConnection( conn );
//			}
//		}
//	}
//	
//	/**
//	 * Sucht im angegebenen GraphModel nach dem ersten Knoten mit dem angegebenen Inhalt.
//	 * Liefert den Knoten zurück, welches den gesuchten Inhalt besitzt, ansonsten null.
//	 *  
//	 * @param <T> Typ des gesuchten Inhalts
//	 * @param graph GraphModel, welcher durchsucht werden soll.
//	 * @param content Gesuchter Inhalt
//	 * @return Knoten, welcher den gesuchten Inhalten besitzt, sonst null.
//	 */
//	private <T> INodeModel<C> findNodeWithContent( IGraphModel<C> graph, T content ) {
//		for( INodeModel<C> node : graph.getNodes() ) {
//			// Wichtig, dass hier equals verwendet wird, da T unbekannt und
//			// alle Möglichkeiten zum Vergleich offen bleiben müssen.
//			if( node.getContent().equals(content) ) 
//				return node;
//		}
//		
//		// Nicht gefunden.
//		return null;
//	}
//	
//	/**
//	 * Sucht im angegebenen GraphModel nach der ersten Verbindung, dessen Anfangs und 
//	 * Endkonten den angegebenen Inhalt besitzen und gibt diesen zurück. Wenn keine 
//	 * Verbindung gefunden wurde, so wird null zurückgegeben.
//	 *  
//	 * @param <S> Typ des Inhalts des Anfangsknotens der gesuchten Verbindung.
//	 * @param <T> Typ des Inhalts des Endknotens der gesuchten Verbindung.
//	 * @param graph GraphModel, welcher durchsucht werden soll
//	 * @param startContent Inhalt des Anfangsknotens der gesuchten Verbindung.
//	 * @param endContent Inhalt des Endknotens der gesuchten Verbindung.
//	 * @return Verbindung, dessen Anfangsknoten und Endknoten den angegebenen Inhalt 
//	 * besitzen, ansonsten null.
//	 */
//	private <S,T> IConnectionModel<C> findConnectionWithContents( IGraphModel<C> graph, S startContent, T endContent ) {
//		for( IConnectionModel<C> conn : graph.getConnections() ) {
//			if( conn.getStartNode().getContent().equals( startContent ) && conn.getEndNode().getContent().equals( endContent ) ) {
//				return conn;
//			}
//		}
//		
//		// Keine Verbindung gefunden.
//		return null;
//	}
}
