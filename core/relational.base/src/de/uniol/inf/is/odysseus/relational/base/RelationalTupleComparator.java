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
package de.uniol.inf.is.odysseus.relational.base;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:12 $ 
 Version: $Revision: 1.3 $
 Log: $Log: RelationalTupleComparator.java,v $
 Log: Revision 1.3  2004/09/16 08:57:12  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.2  2002/01/31 16:14:37  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import java.util.Comparator;
@SuppressWarnings("unchecked")
public class RelationalTupleComparator implements Comparator {

	@Override
	public int compare(Object p0, Object p1) {
		return ((RelationalTuple<?>) p0).compareTo((RelationalTuple<?>) p1);
	}
}