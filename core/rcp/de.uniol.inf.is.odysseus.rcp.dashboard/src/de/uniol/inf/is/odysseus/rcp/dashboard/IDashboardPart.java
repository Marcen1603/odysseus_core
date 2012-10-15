/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.streamconnection.IStreamElementListener;

public interface IDashboardPart extends IStreamElementListener<IStreamObject<?>>, IConfigurationListener {
	
	public boolean init( Configuration configuration );
	public Configuration getConfiguration();
	
	public void createPartControl( Composite parent, ToolBar toolbar );
	
	public void onStart( List<IPhysicalOperator> physicalRoots) throws Exception;
	public void onPause();
	public void onUnpause();
	public void onStop();
	
	public Map<String, String> onSave();
	public void onLoad(Map<String, String> saved);
	
	public void setQueryTextProvider( IDashboardPartQueryTextProvider file );
	public IDashboardPartQueryTextProvider getQueryTextProvider();
}
