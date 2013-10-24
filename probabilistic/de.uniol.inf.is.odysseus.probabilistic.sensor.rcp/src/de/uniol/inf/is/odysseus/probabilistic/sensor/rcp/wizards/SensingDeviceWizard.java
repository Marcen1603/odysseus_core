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
package de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.wizards;

import java.net.URI;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.SensingDevice;
import de.uniol.inf.is.odysseus.probabilistic.sensor.ontology.vocabulary.ODYSSEUS;
import de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.SensorRegistryPlugIn;
import de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.l10n.OdysseusNLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SensingDeviceWizard extends Wizard implements INewWizard {

    private static final Logger LOG = LoggerFactory.getLogger(SensingDeviceWizard.class);

    private SensingDevicePage sensingDevicePage;
    private MeasurementCapabilitiesPage measurementCapabilitiesPage;

    private IWorkbench workbench;

    public SensingDeviceWizard() {
        super();
        setWindowTitle("New Sensing Device");
    }

    @Override
    public void addPages() {
        addPage(sensingDevicePage);
        addPage(measurementCapabilitiesPage);
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.workbench = workbench;
        sensingDevicePage = new SensingDevicePage("Select file name", selection);
        measurementCapabilitiesPage = new MeasurementCapabilitiesPage("Add measurement capabilities", selection, sensingDevicePage);
    }

    @Override
    public boolean performFinish() {
        final IWorkbenchWindow workbenchWindow = this.workbench.getActiveWorkbenchWindow();

        try {
            // final WorkspaceModifyOperation operation = new
            // WorkspaceModifyOperation() {
            // @Override
            // protected void execute(final IProgressMonitor progressMonitor) {
            // try {
            // // final AddressbookService addressbook =
            // // Activator.getAddressbook();
            // // //
            // //
            // NewAddressWizard.this.addressDataPage.getAddress().save(addressbook);
            // }
            // catch (final Exception e) {
            // MessageDialog.openError(workbenchWindow.getShell(),
            // OdysseusNLS.Error, e.getMessage());
            // }
            // finally {
            // progressMonitor.done();
            // }
            //
            // }
            // };

            // this.getContainer().run(false, false, operation);
            SensingDevice device = new SensingDevice(URI.create(ODYSSEUS.NS + sensingDevicePage.getSensingDeviceName()), new SDFSchema("", ProbabilisticTuple.class, sensingDevicePage.getAttributes()));
            for (MeasurementCapability capability : measurementCapabilitiesPage.getMeasurementCapabilities()) {
                device.addCapability(capability);
            }

            SensorRegistryPlugIn.getSensorOntologyService().createSensingDevice(device);
            return true;
        }
        catch (final Exception e) {
            MessageDialog.openError(workbenchWindow.getShell(), OdysseusNLS.Error, e.getMessage());
            LOG.error(e.getMessage(), e);
        }
        return false;

    }

}

@SuppressWarnings("serial")
class CancelException extends Exception {
};
