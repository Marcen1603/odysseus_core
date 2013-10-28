/**
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.ontology.rcp.views;

import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.usermanagement.ISessionEvent;
import de.uniol.inf.is.odysseus.core.server.usermanagement.ISessionListener;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementListener;
import de.uniol.inf.is.odysseus.ontology.model.SensingDevice;
import de.uniol.inf.is.odysseus.ontology.rcp.SensorRegistryPlugIn;
import de.uniol.inf.is.odysseus.rcp.server.views.OperatorDragListener;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SensorRegistryView extends ViewPart implements IDataDictionaryListener, IUserManagementListener, ISessionListener {
    private static final Logger LOG = LoggerFactory.getLogger(SensorRegistryView.class);

    private Composite parent;
    private TreeViewer viewer;
    private StackLayout stackLayout;
    private Label label;

    volatile boolean isRefreshing;
    private boolean refreshEnabled = true;

    @Override
    public void createPartControl(final Composite parent) {
        this.parent = parent;

        this.stackLayout = new StackLayout();
        parent.setLayout(this.stackLayout);

        this.setTreeViewer(new TreeViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI));
        this.getTreeViewer().setContentProvider(new SensingDeviceContentProvider());
        this.getTreeViewer().setLabelProvider(new SensingDeviceContentLabelProvider("source"));

        final int operations = DND.DROP_MOVE;
        final Transfer[] transferTypes = new Transfer[] { LocalSelectionTransfer.getTransfer() };
        this.getTreeViewer().addDragSupport(operations, transferTypes, new OperatorDragListener(this.getTreeViewer(), "STREAM"));

        this.refresh();

        // UserManagement.getInstance().addUserManagementListener(this);
        this.getSite().setSelectionProvider(this.getTreeViewer());

        // Contextmenu
        final MenuManager menuManager = new MenuManager();
        final Menu contextMenu = menuManager.createContextMenu(this.getTreeViewer().getControl());
        // Set the MenuManager
        this.getTreeViewer().getControl().setMenu(contextMenu);
        this.getSite().registerContextMenu(menuManager, this.getTreeViewer());

        this.label = new Label(parent, SWT.NONE);
        this.label.setText("No sources available");

        this.stackLayout.topControl = this.label;
        parent.layout();

    }

    @Override
    public void dispose() {

        super.dispose();
    }

    @Override
    public void setFocus() {
        this.getTreeViewer().getControl().setFocus();
    }

    public TreeViewer getTreeViewer() {
        return this.viewer;
    }

    public final void setSorting(final boolean doSorting) {
        if (doSorting) {
            this.viewer.setSorter(new ViewerSorter());
        }
        else {
            this.viewer.setSorter(null);
        }
    }

    public final boolean isSorting() {
        return this.viewer.getSorter() != null;
    }

    public void refresh() {

        if (this.refreshEnabled) {
            if (this.isRefreshing) {
                return;
            }
            this.isRefreshing = true;
            PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

                @Override
                public void run() {
                    try {
                        SensorRegistryView.this.isRefreshing = false;
                        if (!SensorRegistryView.this.getTreeViewer().getTree().isDisposed()) {
                            final List<SensingDevice> sensingDevices = SensorRegistryPlugIn.getSensorOntologyService().getAllSensingDevices();
                            SensorRegistryView.this.getTreeViewer().setInput(sensingDevices);

                            if (!sensingDevices.isEmpty()) {
                                SensorRegistryView.this.stackLayout.topControl = SensorRegistryView.this.getTreeViewer().getTree();
                            }
                            else {
                                SensorRegistryView.this.stackLayout.topControl = SensorRegistryView.this.label;
                            }
                            SensorRegistryView.this.parent.layout();
                        }
                    }
                    catch (final Exception e) {
                        SensorRegistryView.LOG.error("Exception during setting input for treeViewer in sourcesView", e);
                    }
                }

            });
        }
    }

    protected void setTreeViewer(final TreeViewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void addedViewDefinition(final IDataDictionary sender, final String name, final ILogicalOperator op) {
        this.refresh();
    }

    @Override
    public void removedViewDefinition(final IDataDictionary sender, final String name, final ILogicalOperator op) {
        this.refresh();
    }

    @Override
    public void usersChangedEvent() {
        this.refresh();
    }

    @Override
    public void roleChangedEvent() {
        this.refresh();
    }

    @Override
    public void dataDictionaryChanged(final IDataDictionary sender) {
        this.refresh();
    }

    public boolean isRefreshEnabled() {
        return this.refreshEnabled;
    }

    public void setRefreshEnabled(final boolean refreshEnabled) {
        this.refreshEnabled = refreshEnabled;
    }

    @Override
    public void sessionEventOccured(final ISessionEvent event) {
        this.refresh();
    }

}
