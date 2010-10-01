package de.uniol.inf.is.odysseus.p2p.queryhandling;

import java.io.Serializable;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;

/**
 * 
 * Ein Subplan stellt einen Ausschnitt eines Operatorplans dar. Auf diesen Teilplan koennen fuer die Verarbeitung Gebote abgegeben werden
 * 
 * TODO: Auslagern der Bidding spezifischen Elemente
 * 
 * @author Mart Köhler
 *
 */
public abstract class Subplan implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 794620650300093805L;
	private ILogicalOperator ao;
	private Lifecycle status;
	private String id;
	
	
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
	
	
	public Lifecycle getStatus() {
		return status;
	}

	public void setStatus(Lifecycle status) {
		this.status = status;
	}
	


	public ILogicalOperator getAo() {
		return ao;
	}

	public void setAo(ILogicalOperator ao) {
		this.ao = ao;
	}

	public Subplan(String id, ILogicalOperator ao) {
		super();
		this.ao = ao;
		this.status = Lifecycle.NEW;
		this.id=id;
	}

}
