package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer;

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

import java.io.Serializable;

import org.eclipse.swt.graphics.GC;

import com.vividsolutions.jts.geom.Envelope;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.Buffer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.style.Style;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public interface ILayer extends Serializable {

	public void init(ScreenManager screenManager, SDFSchema schema, SDFAttribute attribute);
	
	public String getName();
	public void setName(String name);
	public String getComplexName();
	public String getQualifiedName();
	public int getSRID();
	
	public String[] getSupportedDatatypes();
		
	public void setBuffer(Buffer puffer);
	
	public Style getStyle();
	
	public void draw(GC gc);	
	
	public int getTupleCount();

	public Envelope getEnvelope();
	
	public LayerConfiguration getConfiguration();
	public void setConfiguration(LayerConfiguration configuration);

	public boolean isActive();
	
	public boolean isGroup();
	
	@Override
	public String toString();

	public void setActive(boolean checked);
}
