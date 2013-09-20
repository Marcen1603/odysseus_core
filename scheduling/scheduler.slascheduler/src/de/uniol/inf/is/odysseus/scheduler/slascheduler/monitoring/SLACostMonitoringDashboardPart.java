/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.scheduler.slascheduler.monitoring;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;

public class SLACostMonitoringDashboardPart extends AbstractDashboardPart implements PaintListener {

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void streamElementRecieved(IPhysicalOperator operator, IStreamObject<?> element, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void punctuationElementRecieved(IPhysicalOperator operator, IPunctuation point, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void securityPunctuationElementRecieved(IPhysicalOperator operator, ISecurityPunctuation sp,
			int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void paintControl(PaintEvent e) {
		// TODO Auto-generated method stub
		
	}
}
