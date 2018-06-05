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

import java.util.Collection;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IWorkbenchPart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.streamconnection.IStreamElementListener;

public interface IDashboardPart extends IStreamElementListener<IStreamObject<?>> {

	public void init( IFile dashboardFile, IProject containingProject, IWorkbenchPart containingPart);
	public void setProject(IProject project);
	public void onLoad(Map<String, String> saved);
	public void onLoadXML( Document document, Element xmlElement );
	public void createPartControl(Composite parent, ToolBar toolbar);
	public void setFocus();
	public void dispose();

	public Map<String, String> onSave();
	public void onSaveXML(Document document, Element xmlElement );
	
	public void onStart(Collection<IPhysicalOperator> physicalRoots) throws Exception;
	public void onStop();
	public void onPause();
	public void onUnpause();
	
	public void onLock();
	public void onUnlock();
	
	public void setQueryTextProvider(IDashboardPartQueryTextProvider file);
	public IDashboardPartQueryTextProvider getQueryTextProvider();
	
	public void setSinkNames( String sinkNames );
	public String getSinkNames();
	
	public boolean isSinkSynchronized();
	public void setSinkSynchronized( boolean doSync );
	
	public void addContext( String key, String value );
	public Optional<String> getContextValue( String key );
	public ImmutableCollection<String> getContextKeys();
	public void removeContext( String key );
	
	public Point getPreferredSize();
	
	public void addListener(IDashboardPartListener listener);
	public void removeListener(IDashboardPartListener listener);
	
	public <T> T getAdapter(Class<T> adapter);
	
	public IDashboardActionBarContributor getEditorActionBarContributor();
	
	
}
