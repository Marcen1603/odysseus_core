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
package de.uniol.inf.is.odysseus.objecttracking.predicate.range;

import java.io.Serializable;
import java.util.List;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


/**
 * A range predicate does not say, if an expression is true or not. It returns
 * the time interval, when the expression is true. If the interval is empty.
 * the expression is never true.
 * 
 * @author André Bolles
 *
 * @param <T>
 */
public interface IRangePredicate<T> extends IClone, Serializable{

	public static String tokenizerDelimiters = " \t\n\r\f + - * / < > '<=' '>=' ^ ( )";
	
	public List<ITimeInterval> evaluate(T input);
	public List<ITimeInterval> evaluate(T left, T right);
	@Override
	public IRangePredicate<T> clone();
	public void init(SDFAttributeList leftSchema, SDFAttributeList rightSchema);
	public long getAdditionalEvaluationDuration();
	
}
