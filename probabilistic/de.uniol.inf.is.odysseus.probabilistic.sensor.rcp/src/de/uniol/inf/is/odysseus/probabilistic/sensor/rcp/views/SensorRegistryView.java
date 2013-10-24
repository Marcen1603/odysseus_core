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
package de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.views;

import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SensorRegistryView extends ViewPart {
    private final List<ISensingDeviceViewData> data = Lists.newArrayList();
    private SensingDeviceTableViewer tableViewer;
    private boolean refreshing;
    private ISensingDeviceViewDataProvider dataProvider;

    @Override
    public void createPartControl(Composite parent) {

        final Composite tableComposite = new Composite(parent, SWT.NONE);
        final TableColumnLayout tableColumnLayout = new TableColumnLayout();
        tableComposite.setLayout(tableColumnLayout);

        tableViewer = new SensingDeviceTableViewer(tableComposite, SWT.MULTI | SWT.FULL_SELECTION);
        tableViewer.setInput(data);
        getSite().setSelectionProvider(tableViewer);

        createContextMenu();

        dataProvider = new SensingDeviceDataViewProvider();
        dataProvider.init(this);
    }

    @Override
    public void dispose() {
        try {
            dataProvider.dispose();
            dataProvider = null;
        }
        catch (final Exception ex) {
            // ignore
        }

        super.dispose();
    }

    public SensingDeviceTableViewer getTableViewer() {
        return tableViewer;
    }

    public void refreshData(String url) {
        Preconditions.checkArgument(url != null, "URL to update sesning device view data must be non-null");

        final Optional<ISensingDeviceViewData> optElement = getData(url);

        if (optElement.isPresent()) {
            if (!PlatformUI.getWorkbench().getDisplay().isDisposed()) {
                PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        if (!tableViewer.getControl().isDisposed()) {
                            tableViewer.refresh(optElement.get());
                        }
                    }

                });
            }
        }
        else {
            throw new IllegalArgumentException("Element " + url + " is not known in sensing device view");
        }
    }

    public void addData(final ISensingDeviceViewData element) {
        Preconditions.checkNotNull(element, "SensingDeviceViewData to add must not be null!");
        Preconditions.checkArgument(!data.contains(element), "SensingDeviceViewData-instance is already added");
        Preconditions.checkArgument(!getData(element.getURL()).isPresent(), "SensingDeviceViewData with URL %s  is already added", element.getURL());

        data.add(element);
    }

    public void removeData(String url) {
        Preconditions.checkArgument(url != null, "URL to remove sensing device view data must be non-null");

        Optional<ISensingDeviceViewData> optData = getData(url);
        if (optData.isPresent()) {
            data.remove(optData.get());
        }
        else {
            throw new IllegalArgumentException("Element with url " + url + " is not known in sensing device view");
        }
    }

    public Optional<ISensingDeviceViewData> getData(String url) {
        Preconditions.checkArgument(url != null, "URL to get sensing device view data must be non-null");

        for (ISensingDeviceViewData dat : data) {
            if (dat.getURL().equals(url)) {
                return Optional.of(dat);
            }
        }

        return Optional.absent();
    }

    @Override
    public void setFocus() {
        tableViewer.getTable().setFocus();
    }

    public void refreshTable() {
        if (refreshing) {
            return;
        }

        if (!PlatformUI.getWorkbench().getDisplay().isDisposed()) {
            refreshing = true;
            dataProvider.onRefresh(this);
            PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

                @Override
                public void run() {
                    if (!tableViewer.getControl().isDisposed()) {
                        tableViewer.refresh();
                    }
                    refreshing = false;
                }

            });
        }
    }

    public void clear() {
        data.clear();
    }

    private void createContextMenu() {
        // Contextmenu
        final MenuManager menuManager = new MenuManager();
        final Menu contextMenu = menuManager.createContextMenu(tableViewer.getTable());
        // Set the MenuManager
        tableViewer.getTable().setMenu(contextMenu);
        getSite().registerContextMenu(menuManager, tableViewer);
    }

}
