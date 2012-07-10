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
package de.uniol.inf.is.odysseus.scars.metadata;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.objecttracking.MVTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

public interface IPredictionFunction<M extends IProbability> {
	
	public void init(SDFSchema scanSchema, SDFSchema timeSchema);
	
	public void predictData(MVTuple<M> scanRootTuple, MVTuple<M> timeTuple, int index);

	public void predictMetadata(M metadata, MVTuple<M> scanRootTuple, MVTuple<M> timeTuple, int index);

	public void setExpressions(PredictionExpression[] expressions);
	
	public void setNoiseMatrix(double[][] noiseMatrix);
	
	public PredictionExpression[] getExpressions();
}
