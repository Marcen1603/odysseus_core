/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 */
package de.uniol.inf.is.odysseus.probabilistic.rcp.chart.menu.command;

import java.util.Collection;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.probabilistic.rcp.chart.ProbabilityChart3D;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.menu.command.AbstractCommand;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ShowProbabilityChart3D extends AbstractCommand {

    private static final Logger LOG = LoggerFactory.getLogger(ShowProbabilityChart3D.class);

    @Override
    public final Object execute(final ExecutionEvent event) throws ExecutionException {
        final Collection<IPhysicalOperator> ops = super.getSelectedOperators(event);

        final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        try {
            for (final IPhysicalOperator op : ops) {
                final ProbabilityChart3D view = (ProbabilityChart3D) activePage.showView("de.offis.chart.charts.probabilitychart3d", "probabilitychart3d", IWorkbenchPage.VIEW_ACTIVATE);
                view.initWithOperator(op);
            }
        }
        catch (final PartInitException e) {
            ShowProbabilityChart3D.LOG.error("Could not open probability chart 3d", e);
            e.printStackTrace();
        }

        return null;
    }
}
