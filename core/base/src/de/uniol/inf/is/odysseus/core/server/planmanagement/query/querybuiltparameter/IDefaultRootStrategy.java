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
package de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;

/**
 * Query parameters can have a default root, that
 * is subscribed to all roots of a query. However,
 * sometimes, the root has to be cloned. In other
 * cases the root has not to be cloned so that
 * a multi-root query ends up in a single physical
 * operator and so on. This interface provides different strategies
 * for returning the default root of a query (clone or not).
 * 
 * @author André Bolles
 *
 */
public interface IDefaultRootStrategy {

	/**
	 * Subscribes the default root to the source.
	 * 
	 * @param defaultRoot
	 * @param source
	 * @return The default root, that has to be subscribed. Some strategies
	 * may clone the default root, so that another reference has to be returned.
	 * This is necessary, since in Query.java the new root of the query has to
	 * set. However, if the subscribed object is already set, because it has not
	 * been cloned, we do not have to set it again. To check this we need the reference
	 * to the default root (clone or not).
	 */
	public IPhysicalOperator connectDefaultRootToSource(ISink<?> defaultRoot, IPhysicalOperator source);
}
