/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
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
package cc.kuka.odysseus.ontology.rcp.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
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

import cc.kuka.odysseus.ontology.common.model.MeasurementCapability;
import cc.kuka.odysseus.ontology.common.model.SensingDevice;
import cc.kuka.odysseus.ontology.rcp.SensorRegistryPlugIn;
import cc.kuka.odysseus.ontology.rcp.actions.CreateConditionAction;
import cc.kuka.odysseus.ontology.rcp.actions.CreateMeasurementCapabilityAction;
import cc.kuka.odysseus.ontology.rcp.actions.CreateMeasurementPropertyAction;
import de.uniol.inf.is.odysseus.rcp.views.OperatorDragListener;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class SensingDevicesView extends ViewPart {// implements
    // IDataDictionaryListener,
    // IUserManagementListener,
    // ISessionListener {
    static final Logger LOG = LoggerFactory.getLogger(SensingDevicesView.class);

    Composite parent;
    TreeViewer viewer;
    StackLayout stackLayout;
    Label label;

    volatile boolean isRefreshing;
    private boolean refreshEnabled = true;

    @Override
    public void createPartControl(final Composite parent) {
        this.parent = parent;

        this.stackLayout = new StackLayout();
        parent.setLayout(this.stackLayout);

        this.setTreeViewer(new TreeViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI));
        this.getTreeViewer().setContentProvider(new SensingDeviceContentProvider());
        this.getTreeViewer().setLabelProvider(new SensingDeviceContentLabelProvider());

        final int operations = DND.DROP_MOVE;
        final Transfer[] transferTypes = new Transfer[] { LocalSelectionTransfer.getTransfer() };
        this.getTreeViewer().addDragSupport(operations, transferTypes, new OperatorDragListener(this.getTreeViewer(), "STREAM"));

        this.refresh();

        // UserManagement.getInstance().addUserManagementListener(this);
        this.getSite().setSelectionProvider(this.getTreeViewer());

        // Contextmenu
        final MenuManager menuManager = new MenuManager();
        final Menu contextMenu = menuManager.createContextMenu(this.getTreeViewer().getControl());

        menuManager.addMenuListener(new IMenuListener() {
            @Override
            public void menuAboutToShow(IMenuManager manager) {
                // IWorkbench wb = PlatformUI.getWorkbench();
                // IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
                if (SensingDevicesView.this.viewer.getSelection().isEmpty()) {
                    return;
                }

                if (SensingDevicesView.this.viewer.getSelection() instanceof IStructuredSelection) {
                    IStructuredSelection selection = (IStructuredSelection) SensingDevicesView.this.viewer.getSelection();
                    Object object = selection.getFirstElement();
                    if (object instanceof SensingDevice) {
                        Action createMeasurementCapabilityAction = new CreateMeasurementCapabilityAction(parent.getDisplay().getActiveShell(), (SensingDevice) object);
                        createMeasurementCapabilityAction.setText("Create Measurement Capability");
                        createMeasurementCapabilityAction.setToolTipText("Creates a new measurement capability");
                        manager.add(createMeasurementCapabilityAction);
                    }
                    else if (object instanceof MeasurementCapability) {
                        Action createConditionAction = new CreateConditionAction(parent.getDisplay().getActiveShell(), (MeasurementCapability) object);
                        createConditionAction.setText("Create Condition");
                        createConditionAction.setToolTipText("Creates a new condition");
                        manager.add(createConditionAction);

                        Action createMeasurementPropertyAction = new CreateMeasurementPropertyAction(parent.getDisplay().getActiveShell(), (MeasurementCapability) object);
                        createMeasurementPropertyAction.setText("Create Measurement Property");
                        createMeasurementPropertyAction.setToolTipText("Creates a new measurement property");
                        manager.add(createMeasurementPropertyAction);
                    }
                }
            }
        });
        menuManager.setRemoveAllWhenShown(true);

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
                        SensingDevicesView.this.isRefreshing = false;
                        if (!SensingDevicesView.this.getTreeViewer().getTree().isDisposed()) {
                            final List<SensingDevice> sensingDevices = SensorRegistryPlugIn.getSensorOntologyService().allSensingDevices();
                            final Collection<Object> entities = new ArrayList<>();
                            entities.addAll(sensingDevices);
                            SensingDevicesView.this.getTreeViewer().setInput(entities);

                            if (!sensingDevices.isEmpty()) {
                                SensingDevicesView.this.stackLayout.topControl = SensingDevicesView.this.getTreeViewer().getTree();
                            }
                            else {
                                SensingDevicesView.this.stackLayout.topControl = SensingDevicesView.this.label;
                            }
                            SensingDevicesView.this.parent.layout();
                        }
                    }
                    catch (final Exception e) {
                        SensingDevicesView.LOG.error("Exception during setting input for treeViewer in sourcesView", e);
                    }
                }

            });
        }
    }

    protected void setTreeViewer(final TreeViewer viewer) {
        this.viewer = viewer;
    }

    // @Override
    // public void addedViewDefinition(final IDataDictionary sender, final
    // String name, final ILogicalOperator op) {
    // this.refresh();
    // }
    //
    // @Override
    // public void removedViewDefinition(final IDataDictionary sender, final
    // String name, final ILogicalOperator op) {
    // this.refresh();
    // }
    //
    // @Override
    // public void usersChangedEvent() {
    // this.refresh();
    // }
    //
    // @Override
    // public void roleChangedEvent() {
    // this.refresh();
    // }
    //
    // @Override
    // public void dataDictionaryChanged(final IDataDictionary sender) {
    // this.refresh();
    // }

    public boolean isRefreshEnabled() {
        return this.refreshEnabled;
    }

    public void setRefreshEnabled(final boolean refreshEnabled) {
        this.refreshEnabled = refreshEnabled;
    }

    // @Override
    // public void sessionEventOccured(final ISessionEvent event) {
    // this.refresh();
    // }

}
