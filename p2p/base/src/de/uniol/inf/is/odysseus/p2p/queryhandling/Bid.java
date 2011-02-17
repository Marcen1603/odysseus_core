/** Copyright [2011] [The Odysseus Team]
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
import java.util.Date;

public abstract class Bid implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6960627612625664351L;
	private Date date;
	private String peerId;
//	private String subplanId;
//	private String queryId;
	private String bid;
	
	public Bid(){};
	
	public Bid(Date date, String peerId, String bid) {
		this.date = date;
		this.peerId = peerId;
		this.bid = bid;
	}

	public Bid(Date date) {
		this.date = date;
		this.bid = null;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

//	public String getSubplanId() {
//		return subplanId;
//	}
//
//	public void setSubplanId(String subplanId) {
//		this.subplanId = subplanId;
//	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	


	public String getPeerId() {
		return peerId;
	}

	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}
//
//	public void setQueryId(String queryId) {
//		this.queryId = queryId;
//	}
//
//	public String getQueryId() {
//		return queryId;
//	}
	
	

}
