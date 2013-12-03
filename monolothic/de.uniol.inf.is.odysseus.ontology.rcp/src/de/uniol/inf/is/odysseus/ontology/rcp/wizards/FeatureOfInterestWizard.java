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

import de.uniol.inf.is.odysseus.ontology.SensorOntologyService;
import de.uniol.inf.is.odysseus.ontology.model.FeatureOfInterest;
import de.uniol.inf.is.odysseus.ontology.model.Property;
import de.uniol.inf.is.odysseus.ontology.rcp.SensorRegistryPlugIn;
import de.uniol.inf.is.odysseus.ontology.rcp.l10n.OdysseusNLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class FeatureOfInterestWizard extends Wizard implements INewWizard {

    private static final Logger LOG = LoggerFactory.getLogger(FeatureOfInterestWizard.class);

    private FeatureOfInterestPage featureOfInterestPage;

    private IWorkbench workbench;

    public FeatureOfInterestWizard() {
        super();
        this.setWindowTitle(OdysseusNLS.NewSensingDevice);
    }

    @Override
    public void addPages() {
        this.addPage(this.featureOfInterestPage);
    }

    @Override
    public void init(final IWorkbench workbench, final IStructuredSelection selection) {
        this.workbench = workbench;
        this.featureOfInterestPage = new FeatureOfInterestPage(OdysseusNLS.DefineSensingDevices, selection);
    }

    @Override
    public boolean performFinish() {
        final IWorkbenchWindow workbenchWindow = this.workbench.getActiveWorkbenchWindow();

        final String name = this.featureOfInterestPage.getFeatureOfInterestName();
        final URI uri = this.featureOfInterestPage.getFeatureOfInterestURI();
        final List<Property> properties = this.featureOfInterestPage.getProperties();

        try {
            final WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {

                @Override
                protected void execute(IProgressMonitor progressMonitor) throws CoreException, InvocationTargetException, InterruptedException {
                    try {
                        final SensorOntologyService ontologyService = SensorRegistryPlugIn.getSensorOntologyService();
                        final FeatureOfInterest featureOfInterest = new FeatureOfInterest(uri, name, properties);

                        ontologyService.createFeatureOfInterest(featureOfInterest);
                    }
                    catch (final Exception e) {
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
            FeatureOfInterestWizard.LOG.error(e.getMessage(), e);
        }
        return false;

    }

}
