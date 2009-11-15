package de.uniol.inf.is.odysseus.p2p.administrationpeer.handler;

/**
 * Eingegangene Anfragen werden in dieser Komponente übersetzt, aufgeteilt und verteilt.
 *  
 * 
 * @author Mart Köhler
 *
 */
public interface IQueryResultHandler {
	
	void handleQueryResult(Object msg, Object namespace);
	
	
}
