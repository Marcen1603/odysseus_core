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
package de.offis.chart.charts;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.offis.chart.charts.datatype.ProbDataType;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype.ViewableDatatypeRegistry;

public class Activator extends AbstractUIPlugin {
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		ViewableDatatypeRegistry.getInstance().register(new ProbDataType());
	}
}
