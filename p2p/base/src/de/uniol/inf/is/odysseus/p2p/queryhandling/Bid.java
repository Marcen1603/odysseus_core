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
import java.util.Date;

import de.uniol.inf.is.odysseus.p2p.IBid;

public abstract class Bid implements Serializable, IBid {
	private static final long serialVersionUID = -6960627612625664351L;
	private Date date;
	private String peerId;
	private int bidValue;

	public Bid() {
	}

	public Bid(Date date, String peerId, int bidValue) {
		this.date = date;
		this.peerId = peerId;
		this.bidValue = bidValue;
	}

	public Bid(Date date) {
		this.date = date;
	}

	@Override
	public int getBidValue() {
		return bidValue;
	}

	public void setBidValue(int bidValue) {
		this.bidValue = bidValue;
	}

	@Override
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String getPeerId() {
		return peerId;
	}

	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}

	@Override
	public int compareTo(IBid o) {
		if (this.getBidValue() < o.getBidValue()){
			return -1;
		}
		if (this.getBidValue() > o.getBidValue()){
			return 1;
		}
		return 0;
	}

}
