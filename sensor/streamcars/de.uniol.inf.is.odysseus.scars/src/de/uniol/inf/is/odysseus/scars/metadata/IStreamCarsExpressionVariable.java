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
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaIndexPath;
import de.uniol.inf.is.odysseus.objecttracking.MVTuple;
import de.uniol.inf.is.odysseus.relational.base.schema.TupleIndexPath;

/**
 * 
 * @author Benjamin G
 */
public interface IStreamCarsExpressionVariable {

	public IStreamCarsExpression getParent();

	public String getName();

	public String getSourceName();

	public String getMetadataInfo();

	public Object getValue();

	public double getDoubleValue();

	public int[] getPath();

	public int[] getPath(boolean copy);

	public void init(SDFSchema schema);

	public boolean isSchemaVariable();

	public boolean isSchemaVariable(SDFSchema schema);

	public boolean isVirtual();

	public boolean hasMetadataInfo();

	public boolean isInList(int[] pathToList);

	public boolean isInList(TupleIndexPath pathToList);

	public SchemaIndexPath getSchemaIndexPath();

	public void replaceVaryingIndex(int index);

	public void replaceVaryingIndex(int index, boolean copy);

	public void bind(Object value);

	public void bindTupleValue(MVTuple<?> tuple);

	public String getNameWithoutMetadata();
	
	public void reset();



}
