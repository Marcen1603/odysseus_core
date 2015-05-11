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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractPartialAggregate;


public class AvgSumPartialAggregate<R> extends AbstractPartialAggregate<R> {

	private static final long serialVersionUID = -1345991906490443262L;
	
	double aggValue;
	int aggCount;
	
	public AvgSumPartialAggregate(Number initAggValue, int initCount){
        if (initAggValue != null) {
            this.aggValue = initAggValue.doubleValue();
            this.aggCount = initCount;
        }
	}
	
	public AvgSumPartialAggregate(
			AvgSumPartialAggregate<R> avgSumPartialAggregate) {
		this.aggCount = avgSumPartialAggregate.aggCount;
		this.aggValue = avgSumPartialAggregate.aggValue;
	}

	public AvgSumPartialAggregate<R> merge(AvgSumPartialAggregate<R> toMerge){
		this.aggValue += toMerge.aggValue;
		this.aggCount += toMerge.aggCount;
		return this;
	}
	
	public Double getAggValue(){
		return aggValue;
	}
	
	public int getCount(){
		return aggCount;
	}
	
	public AvgSumPartialAggregate<R> addAggValue(Number toAdd){
        if (toAdd != null) {
            this.aggValue += toAdd.doubleValue();
            aggCount++;
        }
		return this;
	}
	
	public void setAggValue(Number newAggValue, int newCount){
        if (newAggValue != null) {
            this.aggValue = newAggValue.doubleValue();
            aggCount = newCount;
        }
	}
	
	@Override
	public AvgSumPartialAggregate<R> clone(){
		return new AvgSumPartialAggregate<R>(this);
	}
	
	@Override
	public String toString() {
		return "SUM= "+aggValue+" COUNT="+aggCount;
	}	

}
