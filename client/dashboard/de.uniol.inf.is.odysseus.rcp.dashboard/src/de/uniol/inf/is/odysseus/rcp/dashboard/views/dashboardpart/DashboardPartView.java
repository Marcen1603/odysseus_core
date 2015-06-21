/*******************************************************************************
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.views.dashboardpart;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardHandlerException;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartRegistry;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.handler.XMLDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider.ResourceFileQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.OdysseusScriptEditor;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class DashboardPartView extends ViewPart {
    public static final String VIEW_ID = DashboardPlugIn.VIEW_DASHBOARDPARTVIEW_ID;

    private static final Logger LOG = LoggerFactory.getLogger(DashboardPartView.class);
    private TreeViewer viewer;

    @Override
    public void createPartControl(Composite parent) {
        parent.setLayout(new FillLayout());

        setTreeViewer(new TreeViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI));
        getTreeViewer().setContentProvider(new DashboardPartViewContentProvider());
        getTreeViewer().setLabelProvider(new DashboardPartViewLabelProvider());
        refresh();

        getSite().setSelectionProvider(getTreeViewer());

        MenuManager menuManager = new MenuManager();
        Menu contextMenu = menuManager.createContextMenu(getTreeViewer().getControl());
        getTreeViewer().getControl().setMenu(contextMenu);
        getSite().registerContextMenu(menuManager, getTreeViewer());

        getTreeViewer().addDoubleClickListener(new IDoubleClickListener() {

            @Override
            public void doubleClick(DoubleClickEvent event) {

                if (event.getSelection().isEmpty()) {
                    return;
                }
                if (event.getSelection() instanceof IStructuredSelection) {
                    IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                    for (Iterator<?> iterator = selection.iterator(); iterator.hasNext();) {
                        IConfigurationElement dashboardPart = (IConfigurationElement) iterator.next();
                        try {
                            createDashboardPart(dashboardPart);
                        }
                        catch (InstantiationException | CoreException | DashboardHandlerException e) {
                            LOG.error(e.getMessage(), e);
                        }

                    }
                }

            }
        });
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void setFocus() {
        getTreeViewer().getControl().setFocus();
    }

    public TreeViewer getTreeViewer() {
        return viewer;
    }

    public void refresh() {
        if (!PlatformUI.getWorkbench().getDisplay().isDisposed()) {
            PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

                @Override
                public void run() {
                    try {
                        IConfigurationElement[] extensions = Platform.getExtensionRegistry().getConfigurationElementsFor("de.uniol.inf.is.odysseus.rcp.DashboardPart");
                        getTreeViewer().setInput(extensions);

                    }
                    catch (Exception e) {
                        LOG.error("Exception during setting input for treeViewer in dashboard part list", e);
                    }
                }

            });
        }
    }

    protected void setTreeViewer(TreeViewer viewer) {
        this.viewer = viewer;
    }

    public void createDashboardPart(IConfigurationElement element) throws InstantiationException, CoreException, DashboardHandlerException {
        IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (editor instanceof OdysseusScriptEditor) {
            ResourceFileQueryTextProvider query = new ResourceFileQueryTextProvider(((OdysseusScriptEditor) editor).getFile());
            String name = element.getAttribute("name");
            final IDashboardPart dashboardPart = DashboardPartRegistry.createDashboardPart(name);
            final IPath path = query.getFile().getParent().getFullPath().append("New DashboardPart" + "." + DashboardPlugIn.DASHBOARD_PART_EXTENSION);
            final IFile dashboardPartFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);

            dashboardPartFile.create(null, true, null);

            dashboardPart.setQueryTextProvider(query);

            final IDashboardPartHandler handler = new XMLDashboardPartHandler();
            handler.save(dashboardPart, dashboardPartFile);

            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(dashboardPartFile), DashboardPlugIn.DASHBOARD_PART_EDITOR_ID, true);
        }
    }
}
