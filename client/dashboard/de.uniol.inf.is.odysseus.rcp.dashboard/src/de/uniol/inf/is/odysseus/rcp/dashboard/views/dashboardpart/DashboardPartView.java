/*******************************************************************************
 * Copyright (C) 2015  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.views.dashboardpart;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class DashboardPartView extends ViewPart {

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

}
