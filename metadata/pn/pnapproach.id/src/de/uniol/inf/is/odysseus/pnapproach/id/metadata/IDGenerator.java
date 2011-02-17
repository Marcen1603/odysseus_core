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
package de.uniol.inf.is.odysseus.pnapproach.id.metadata;

import java.util.ArrayList;
import java.util.List;

public class IDGenerator {

	private static long ID = 1;
	
	/**
	 * this variable will be used, if a wildcard is necessary for an id
	 * we will use the object reference and not the value
	 */
	private static Long wildcard = new Long(-1);
	
	private static long genID(){
		return ++ID;
	}
	
	public static List<Long> nextID(){
		ArrayList<Long> idList = new ArrayList<Long>();
		idList.add(new Long(genID()));
		return idList;
	}
	
	public static Long getWildcard(){
		return wildcard;
	}
}
