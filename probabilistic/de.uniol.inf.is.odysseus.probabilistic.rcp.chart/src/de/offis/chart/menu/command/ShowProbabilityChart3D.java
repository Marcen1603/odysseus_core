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
package de.offis.chart.menu.command;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.offis.chart.charts.ProbabilityChart3D;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.menu.command.AbstractCommand;

public class ShowProbabilityChart3D extends AbstractCommand {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IPhysicalOperator op = super.getSelectedOperator(event);
		
		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			ProbabilityChart3D view = (ProbabilityChart3D)activePage.showView("de.offis.chart.charts.probabilitychart3d", "probabilitychart3d", IWorkbenchPage.VIEW_ACTIVATE);
			view.initWithOperator(op);
		} catch (PartInitException e) {
			e.printStackTrace();
		}			
		
		
		return null;
	}
}
