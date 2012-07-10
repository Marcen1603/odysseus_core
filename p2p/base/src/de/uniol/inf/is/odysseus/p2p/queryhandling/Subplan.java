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
package de.uniol.inf.is.odysseus.p2p.queryhandling;

import java.io.Serializable;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * 
 * Ein Subplan stellt einen Ausschnitt eines Operatorplans dar. Auf diesen
 * Teilplan koennen fuer die Verarbeitung Gebote abgegeben werden
 * 
 * 
 * @author Mart Koehler, Marco Grawunder
 * 
 */
public class Subplan implements Serializable {

	private static final long serialVersionUID = 794620650300093805L;
	private ILogicalOperator ao;
	private Lifecycle status;
	private String id;
	private String peerId = null;
	String responseSocket;
	private IPhysicalQuery query;
	
	protected ArrayList<Bid> biddings = new ArrayList<Bid>();

	public Subplan(String id, ILogicalOperator ao) {
		super();
		this.ao = ao;
		this.status = Lifecycle.NEW;
		this.id = id;
	}
	
	public void setQuery( IPhysicalQuery query ){
		this.query = query;
	}
	
	public IPhysicalQuery getQuery() {
		return query;
	}
	
	public ArrayList<Bid> getBiddings() {
		return biddings;
	}
	
	public synchronized void addBit(Bid bid){
		biddings.add(bid);
	}

	public void setBiddings(ArrayList<Bid> biddings) {
		this.biddings = biddings;
	}

	public String getId() {
		return id;
	}

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

	public String getResponseSocket() {
		return responseSocket;
	}

	public void setResponseSocket(String responseSocket) {
		this.responseSocket = responseSocket;
	}

}
