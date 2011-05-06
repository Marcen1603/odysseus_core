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
package de.uniol.inf.is.odysseus.intervalapproach;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.physicaloperator.ILeftMergeFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public abstract class LeftMergeFunction<T extends IClone> implements ILeftMergeFunction<T>{

	protected SDFAttributeList leftSchema;
	protected SDFAttributeList rightSchema;
	protected SDFAttributeList resultSchema;

	
	public LeftMergeFunction(SDFAttributeList leftSchema, SDFAttributeList rightSchema, SDFAttributeList resultSchema){
		this.leftSchema = leftSchema;
		this.rightSchema = rightSchema;
		this.resultSchema = resultSchema;
	}
	
	public LeftMergeFunction(LeftMergeFunction<T> mf){
		this.leftSchema = mf.leftSchema.clone();
		this.rightSchema = mf.rightSchema.clone();
		this.resultSchema = mf.resultSchema.clone();
	}
	
	@Override
	public abstract T merge(T left, T right);
	
	public abstract T createLeftFilledUp(T left);
	
	@Override
	public LeftMergeFunction<T> clone() {
		throw new RuntimeException("Clone Not implemented yet");
	}
}
