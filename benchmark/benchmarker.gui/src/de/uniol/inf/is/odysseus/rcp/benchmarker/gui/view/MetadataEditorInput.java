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
package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.view;

import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.Benchmark;

/**
 * Diese Klasse beinhaltet den Input für die View MetadataEditorPart
 * 
 * @author Stefanie Witzke
 * 
 */
public class MetadataEditorInput implements IEditorInput {
	private Map<String, String> metadata;
	private Benchmark benchmark;

	public MetadataEditorInput(Map<String, String> map, Benchmark benchmark) {
		this.metadata = map;
		this.benchmark = benchmark;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public String getName() {
		return "Metadata";
	}

	@Override
	public String getToolTipText() {
		return getName();
	}

	@Override
	public int hashCode() {
		return getId();
	}

	private int getId() {
		return benchmark.getParam().getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetadataEditorInput other = (MetadataEditorInput) obj;
		if (getId() == other.getId()) {
			return true;
		}
		return false;
	}
}
