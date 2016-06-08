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
package cc.kuka.odysseus.ontology.rcp.wizards;

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

import cc.kuka.odysseus.ontology.common.SensorOntologyService;
import cc.kuka.odysseus.ontology.common.model.FeatureOfInterest;
import cc.kuka.odysseus.ontology.common.model.Property;
import cc.kuka.odysseus.ontology.rcp.SensorRegistryPlugIn;
import cc.kuka.odysseus.ontology.rcp.l10n.OdysseusNLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
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
                protected void execute(final IProgressMonitor progressMonitor) throws CoreException, InvocationTargetException, InterruptedException {
                    try {
                        final SensorOntologyService ontologyService = SensorRegistryPlugIn.getSensorOntologyService();
                        final FeatureOfInterest featureOfInterest = new FeatureOfInterest(uri, name, properties);

                        ontologyService.create(featureOfInterest);
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
