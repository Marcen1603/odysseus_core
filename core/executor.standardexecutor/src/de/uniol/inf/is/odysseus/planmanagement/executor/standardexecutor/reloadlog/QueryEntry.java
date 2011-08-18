/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.planmanagement.executor.standardexecutor.reloadlog;

/**
 * 
 * @author Dennis Geesen
 * Created at: 18.08.2011
 */
public class QueryEntry {
	public String parserID;
	public String transCfgID;
	public String query;
	public String username;

	public QueryEntry getCopy() {
		QueryEntry qe = new QueryEntry();
		qe.parserID = this.parserID;
		qe.transCfgID = this.transCfgID;
		qe.query = this.query;
		qe.username = this.username;
		return qe;
	}

	public String toString() {
		String newline = System.getProperty("line.separator");
		String s = "#PARSER " + this.parserID;
		s = s + newline;
		s = s + "#TRANSCFG " + this.transCfgID;
		s = s + newline;
		s = s + "#ADDQUERY";
		s = s + newline;
		s=  s + this.query;
		s = s + newline;
		return s;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((parserID == null) ? 0 : parserID.hashCode());
		result = prime * result + ((query == null) ? 0 : query.hashCode());
		result = prime * result + ((transCfgID == null) ? 0 : transCfgID.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QueryEntry other = (QueryEntry) obj;
		if (parserID == null) {
			if (other.parserID != null)
				return false;
		} else if (!parserID.equals(other.parserID))
			return false;
		if (query == null) {
			if (other.query != null)
				return false;
		} else if (!query.equals(other.query))
			return false;
		if (transCfgID == null) {
			if (other.transCfgID != null)
				return false;
		} else if (!transCfgID.equals(other.transCfgID))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	
}
