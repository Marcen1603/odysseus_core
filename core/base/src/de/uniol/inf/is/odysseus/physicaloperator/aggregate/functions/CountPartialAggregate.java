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
package de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;


public class CountPartialAggregate<T> implements IPartialAggregate<T> {
	int count = 0;

	
	public CountPartialAggregate(int count) {
		this.count = count;
	}

	public CountPartialAggregate(CountPartialAggregate<T> countPartialAggregate) {
		this.count = countPartialAggregate.count;
	}

	public int getCount() {
		return count;
	}
	
	public void add(){
		//System.out.println("CountPartialAggregate "+count+" --> "+(count+1));
		count=count+1;
	}
	
	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer("CountPartialAggregate (").append(hashCode()).append(")").append(count);
		return ret.toString();
	}
	
	@Override
	public CountPartialAggregate<T> clone(){
		return new CountPartialAggregate<T>(this);
	}
}
