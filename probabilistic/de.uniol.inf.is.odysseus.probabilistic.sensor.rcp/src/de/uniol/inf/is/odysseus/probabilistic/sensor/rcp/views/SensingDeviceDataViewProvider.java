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

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.ui.handlers.IHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.probabilistic.sensor.SensorOntologyService;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.SensingDevice;
import de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.SensorRegistryPlugIn;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SensingDeviceDataViewProvider implements ISensingDeviceViewDataProvider, IDoubleClickListener, KeyListener {
    private static final Logger LOG = LoggerFactory.getLogger(SensingDeviceDataViewProvider.class);
    private SensorRegistryView view;

    @Override
    public void init(SensorRegistryView view) {
        this.view = view;
        this.view.getTableViewer().addDoubleClickListener(this);
        this.view.getTableViewer().getTable().addKeyListener(this);
    }

    @Override
    public void dispose() {
        this.view.getTableViewer().removeDoubleClickListener(this);
        this.view.getTableViewer().getTable().removeKeyListener(this);
        this.view = null;
    }

    @Override
    public void doubleClick(DoubleClickEvent event) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.keyCode == 127) { // delete-key
        }
    }

    @Override
    public void onRefresh(SensorRegistryView sender) {
        SensorOntologyService ontology = SensorRegistryPlugIn.getSensorOntologyService();

        List<SensingDevice> sensingDevices = ontology.getAllSensingDevices();
        List<ISensingDeviceViewData> transformed = Lists.transform(sensingDevices, new Function<SensingDevice, ISensingDeviceViewData>() {
            @Override
            public ISensingDeviceViewData apply(SensingDevice sensingDevice) {
                return new SensingDeviceViewData(sensingDevice);
            }
        });

        sender.clear();
        for (ISensingDeviceViewData data : transformed) {
            sender.addData(data);
        }
        sender.refreshTable();
    }

    @SuppressWarnings("unused")
    private void executeCommand(String cmdID) {
        IHandlerService handlerService = (IHandlerService) view.getSite().getService(IHandlerService.class);
        try {
            handlerService.executeCommand(cmdID, null);
        }
        catch (Exception ex) {
            LOG.error("Exception during executing command {}.", cmdID, ex);
        }
    }

}
