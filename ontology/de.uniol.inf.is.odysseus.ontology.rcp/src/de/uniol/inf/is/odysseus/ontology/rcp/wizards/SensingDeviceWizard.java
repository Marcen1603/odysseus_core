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

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.ontology.SensorOntologyService;
import de.uniol.inf.is.odysseus.ontology.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.ontology.model.SensingDevice;
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
        final String name = this.sensingDevicePage.getSensingDeviceName();
        final URI uri = this.sensingDevicePage.getSensingDeviceURI();
        @SuppressWarnings("unused")
        final List<SDFAttribute> attributes = this.sensingDevicePage.getAttributes();
        final List<MeasurementCapability> measurementCapabilities = this.measurementCapabilitiesPage.getMeasurementCapabilities();
        try {
            final WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {

                @Override
                protected void execute(IProgressMonitor progressMonitor) throws CoreException, InvocationTargetException, InterruptedException {
                    try {
                        final SensorOntologyService ontologyService = SensorRegistryPlugIn.getSensorOntologyService();
                        final SensingDevice sensingDevice = new SensingDevice(uri, name);
                        for (MeasurementCapability measurementCapability : measurementCapabilities) {
                            sensingDevice.addMeasurementCapability(measurementCapability);
                        }
                        ontologyService.createSensingDevice(sensingDevice);
                    }
                    catch (final Exception e) {
						LOG.error(e.getMessage(), e);
                        MessageDialog.openError(workbenchWindow.getShell(), OdysseusNLS.Error, e.getMessage());
                    }
                    finally {
                        progressMonitor.done();
                    }
                }
            };
            this.getContainer().run(false, false, operation);
            return true;
        }
        catch (final Exception e) {
            MessageDialog.openError(workbenchWindow.getShell(), OdysseusNLS.Error, e.getMessage());
            SensingDeviceWizard.LOG.error(e.getMessage(), e);
        }
        return false;

    }

}
