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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer;

import java.io.Serializable;

import org.eclipse.swt.graphics.GC;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public interface ILayer extends Serializable {

	public void init(ScreenManager screenManager, SDFSchema schema, SDFAttribute attribute);
	
	public String getName();
	public String getComplexName();
	public String getQualifiedName();
	public String getSRID();
	
	public String[] getSupprtedDatatypes();
	
	public void addTuple(Tuple<?> tuple);
	
	public Style getStyle();
	
	public void draw(GC gc);	
	
	public void removeLast();
	
	public int getTupleCount();
	
	public LayerConfiguration getConfiguration();
	public void setConfiguration(LayerConfiguration configuration);

	public boolean isActive();
	
}
