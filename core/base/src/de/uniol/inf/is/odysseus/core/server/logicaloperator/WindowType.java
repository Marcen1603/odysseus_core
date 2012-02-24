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
/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

public enum WindowType {
	TIME,
	TUPLE,
	UNBOUNDED
}
//    PERIODIC_TIME_WINDOW, // Zeit mit SLIDE
//    PERIODIC_TUPLE_WINDOW, // Tupel mit SLIDE
//    
//    SLIDING_TIME_WINDOW, // Zeit mit ADVANCE (ADVANCE == 1)
//    JUMPING_TIME_WINDOW, // Zeit mit ADVANCE
//    FIXED_TIME_WINDOW, // Zeit mit ADVANCE (ADVANCE == RANGE)
//    
//    SLIDING_TUPLE_WINDOW, // ElementBasiertes (Advance == 1)
//    JUMPING_TUPLE_WINDOW, // ElementBasiertes mit Advance (im Moment nur CQL)
//    START_END_PREDICATE_WINDOW,// Start-/Ende-Praedikatfenster (im Moment nur Streaming SPARQL) 
//    UNBOUNDED} //unbounded (end timestamp = \infty)