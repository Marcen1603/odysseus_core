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
package de.uniol.inf.is.odysseus.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;

public class QueryAccessAO extends AccessAO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3737401487581114190L;

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = super.hashCode();
//		result = prime * result + ((query == null) ? 0 : query.hashCode());
//		return result;
//	}

//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (!super.equals(obj))
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		QueryAccessAO other = (QueryAccessAO) obj;
//		if (query == null) {
//			if (other.query != null)
//				return false;
//		} else if (!query.equals(other.query))
//			return false;
//		return true;
//	}

	private String query;
	
	public QueryAccessAO() {
		super();
	}
	
	public QueryAccessAO(QueryAccessAO ao) {
		super(ao);
		this.query = ao.query;
	}
	
	public QueryAccessAO(SDFSource source) {
		super(source);
		this.query = source.getStringAttribute("query");
	}
	
	public String getQuery() {
		return this.query;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
}
