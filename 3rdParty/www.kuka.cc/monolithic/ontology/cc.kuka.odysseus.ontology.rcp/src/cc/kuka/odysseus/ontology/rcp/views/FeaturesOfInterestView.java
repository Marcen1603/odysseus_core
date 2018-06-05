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

import cc.kuka.odysseus.ontology.common.model.FeatureOfInterest;
import cc.kuka.odysseus.ontology.rcp.SensorRegistryPlugIn;
import cc.kuka.odysseus.ontology.rcp.actions.CreatePropertyAction;
import de.uniol.inf.is.odysseus.rcp.views.OperatorDragListener;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class FeaturesOfInterestView extends ViewPart {// implements
    // IDataDictionaryListener,
    // IUserManagementListener,
    // ISessionListener {
    static final Logger LOG = LoggerFactory.getLogger(FeaturesOfInterestView.class);

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
        this.getTreeViewer().setContentProvider(new FeatureOfInterestContentProvider());
        this.getTreeViewer().setLabelProvider(new FeatureOfInterestContentLabelProvider());

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
                if (FeaturesOfInterestView.this.viewer.getSelection().isEmpty()) {
                    return;
                }

                if (FeaturesOfInterestView.this.viewer.getSelection() instanceof IStructuredSelection) {
                    IStructuredSelection selection = (IStructuredSelection) FeaturesOfInterestView.this.viewer.getSelection();
                    Object object = selection.getFirstElement();
                    if (object instanceof FeatureOfInterest) {
                        Action createPropertyAction = new CreatePropertyAction(parent.getDisplay().getActiveShell(), (FeatureOfInterest) object);
                        createPropertyAction.setText("Create Property");
                        createPropertyAction.setToolTipText("Creates a new property");
                        manager.add(createPropertyAction);
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
                        FeaturesOfInterestView.this.isRefreshing = false;
                        if (!FeaturesOfInterestView.this.getTreeViewer().getTree().isDisposed()) {
                            final List<FeatureOfInterest> featuresOfInterest = SensorRegistryPlugIn.getSensorOntologyService().allFeaturesOfInterests();
                            final Collection<Object> entities = new ArrayList<>();
                            entities.addAll(featuresOfInterest);
                            FeaturesOfInterestView.this.getTreeViewer().setInput(entities);

                            if (!featuresOfInterest.isEmpty()) {
                                FeaturesOfInterestView.this.stackLayout.topControl = FeaturesOfInterestView.this.getTreeViewer().getTree();
                            }
                            else {
                                FeaturesOfInterestView.this.stackLayout.topControl = FeaturesOfInterestView.this.label;
                            }
                            FeaturesOfInterestView.this.parent.layout();
                        }
                    }
                    catch (final Exception e) {
                        FeaturesOfInterestView.LOG.error("Exception during setting input for treeViewer in sourcesView", e);
                    }
                }

            });
        }
    }

    protected void setTreeViewer(final TreeViewer viewer) {
        this.viewer = viewer;
    }

    public boolean isRefreshEnabled() {
        return this.refreshEnabled;
    }

    public void setRefreshEnabled(final boolean refreshEnabled) {
        this.refreshEnabled = refreshEnabled;
    }

}
