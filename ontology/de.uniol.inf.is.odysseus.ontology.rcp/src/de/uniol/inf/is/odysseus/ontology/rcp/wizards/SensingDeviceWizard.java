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
package de.uniol.inf.is.odysseus.ontology.rcp.wizards;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.ontology.SensorOntologyService;
import de.uniol.inf.is.odysseus.ontology.rcp.SensorRegistryPlugIn;
import de.uniol.inf.is.odysseus.ontology.rcp.l10n.OdysseusNLS;

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
        this.setWindowTitle(OdysseusNLS.NewSensingDevice);
    }

    @Override
    public void addPages() {
        this.addPage(this.sensingDevicePage);
        this.addPage(this.measurementCapabilitiesPage);
    }

    @Override
    public void init(final IWorkbench workbench, final IStructuredSelection selection) {
        this.workbench = workbench;
        this.sensingDevicePage = new SensingDevicePage(OdysseusNLS.DefineSensingDevices, selection);
        this.measurementCapabilitiesPage = new MeasurementCapabilitiesPage(OdysseusNLS.DefineMeasurementCapabilities, selection, this.sensingDevicePage);
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
            final SensorOntologyService ontologyService = SensorRegistryPlugIn.getSensorOntologyService();
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
            // final SensingDevice device = new
            // SensingDevice(URI.create(ODYSSEUS.NS +
            // this.sensingDevicePage.getSensingDeviceName()), new SDFSchema("",
            // ProbabilisticTuple.class,
            // this.sensingDevicePage.getAttributes()));
            // for (final MeasurementCapability capability :
            // this.measurementCapabilitiesPage.getMeasurementCapabilities()) {
            // device.addMeasurementCapability(capability);
            // }
            //
            // SensorRegistryPlugIn.getSensorOntologyService().createSensingDevice(device);
            return true;
        }
        catch (final Exception e) {
            MessageDialog.openError(workbenchWindow.getShell(), OdysseusNLS.Error, e.getMessage());
            SensingDeviceWizard.LOG.error(e.getMessage(), e);
        }
        return false;

    }

}

class CancelException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 4072657522498739025L;
};
