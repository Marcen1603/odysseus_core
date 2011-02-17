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
package de.uniol.inf.is.odysseus.action.dataSources.generator;

public class Tool {
	private int limit1;
	private int limit2;
	private double usageRate;
	private int id;

	public Tool(int limit1, int limit2, int toolID) {
		this.limit1 = limit1;
		this.limit2 = limit2;
		this.id = toolID;
		
		this.usageRate = 0.0d;
	}
	
	public void increaseUsageRate(double value){
		this.usageRate += value;
	}
	
	public double getUsageRate() {
		return usageRate;
	}
	
	public boolean isLimit1Hit(){
		return this.usageRate >= this.limit1;
	}
	
	public boolean isLimit2Hit(){
		return this.usageRate >= this.limit2;
	}
	
	public int getId() {
		return id;
	}
	
	public int getLimit1() {
		return limit1;
	}
	
	public int getLimit2() {
		return limit2;
	}
}
