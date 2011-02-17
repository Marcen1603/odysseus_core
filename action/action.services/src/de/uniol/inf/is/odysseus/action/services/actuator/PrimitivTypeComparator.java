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
package de.uniol.inf.is.odysseus.action.services.actuator;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper which checks if a class is the primitiv wrapper of the other one
 * @author Simon Flandergan
 *
 */
public class PrimitivTypeComparator {
	
private static Map<Class<?>,Class<?>> primitivClassMapping;

	public static boolean sameType (Class<?>c1, Class<?> c2){
		if (primitivClassMapping == null){
			primitivClassMapping = new HashMap<Class<?>, Class<?>>();
			primitivClassMapping.put(Double.class, double.class);
			primitivClassMapping.put(Float.class, float.class);
			primitivClassMapping.put(Long.class, long.class);
			primitivClassMapping.put(Integer.class, int.class);
			primitivClassMapping.put(Short.class, short.class);
			primitivClassMapping.put(Byte.class, byte.class);
			primitivClassMapping.put(Character.class, char.class);
			primitivClassMapping.put(Boolean.class, boolean.class);
		}
		
		if (c1 == c2){
			return true;
		}else if (c1 == null || c2 == null){
			return false;	
		}else if (primitivClassMapping.get(c1) == c2 || primitivClassMapping.get(c2) == c1){
			return true;
		}
		return false;
	}
	
}
