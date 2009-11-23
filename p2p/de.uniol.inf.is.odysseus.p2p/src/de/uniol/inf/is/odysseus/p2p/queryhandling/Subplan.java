package de.uniol.inf.is.odysseus.p2p.queryhandling;

import java.io.Serializable;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;

/**
 * 
 * Ein Subplan stellt einen Ausschnitt eines Operatorplans dar. Auf diesen Teilplan koennen fuer die Verarbeitung Gebote abgegeben werden
 * 
 * TODO: Auslagern der Bidding spezifischen Elemente
 * 
 * @author Mart Köhler
 *
 */
public class Subplan implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 794620650300093805L;
	private AbstractLogicalOperator ao;
	private SubplanStatus status;
	private String id;
	
	//OPEN = Subplan wurde noch keinem Peer zugeteilt.
	//CLOSED = Subplan wurde breits einem Peer zugeteilt.
	//FAILED = Verteilung ist fehlgeschlagen
	public enum SubplanStatus{
		OPEN, CLOSED, FAILED
	}
	
	protected ArrayList<Bid> biddings = new ArrayList<Bid>();
	
	public ArrayList<Bid> getBiddings() {
		return biddings;
	}

	public void setBiddings(ArrayList<Bid> biddings) {
		this.biddings = biddings;
	}
	

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String peerId = null;
	

	
	public String getPeerId() {
		return peerId;
	}

	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}
	
	
	public SubplanStatus getStatus() {
		return status;
	}

	public void setStatus(SubplanStatus status) {
		this.status = status;
	}
	


	public AbstractLogicalOperator getAo() {
		return ao;
	}

	public void setAo(AbstractLogicalOperator ao) {
		this.ao = ao;
	}

	public Subplan(String id, AbstractLogicalOperator ao) {
		super();
		this.ao = ao;
		this.status = SubplanStatus.OPEN;
		this.id=id;
	}

}
