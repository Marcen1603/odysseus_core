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
package de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.util.CopyPhysicalGraphVisitor;

public class CloneDefaultRootStrategy implements IDefaultRootStrategy{

	@SuppressWarnings({"unchecked","rawtypes"})
	@Override
	public IPhysicalOperator connectDefaultRootToSource(ISink<?> defaultRoot,
			IPhysicalOperator source) {
		// the default root can be a whole plan. So the whole plan must be cloned.
		CopyPhysicalGraphVisitor copyVis = new CopyPhysicalGraphVisitor<IPhysicalOperator>();
		AbstractGraphWalker walker = new AbstractGraphWalker();
		walker.prefixWalkPhysical(defaultRoot, copyVis);
		
		
		ISink<?> defaultRootCopy = (ISink<?>)copyVis.getResult();
		
		((ISource) source).connectSink((ISink) defaultRootCopy, 0, 0, source.getOutputSchema());
		return defaultRootCopy;
	}

}
