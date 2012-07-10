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
package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public final class Dashboard {

	private List<DashboardPartPlacement> dashboardParts = Lists.newArrayList();

	public void add(DashboardPartPlacement partPlace) {
		Preconditions.checkNotNull(partPlace, "Placement for Dashboard Part must not be null!");
		Preconditions.checkArgument(!dashboardParts.contains(partPlace), "Dashboard part placement %s already added!", partPlace);

		dashboardParts.add(partPlace);
	}

	public void remove(DashboardPartPlacement partPlace) {
		Preconditions.checkNotNull(partPlace, "Placement for Dashboard Part must not be null!");

		dashboardParts.remove(partPlace);
	}

	public ImmutableList<DashboardPartPlacement> getDashboardPartPlacements() {
		return ImmutableList.copyOf(dashboardParts);
	}

	public void createPartControl(Composite parent, ToolBar toolBar) {
		Composite dashboardComposite = new Composite(parent, SWT.BORDER);
		dashboardComposite.setLayout(new FormLayout());
		dashboardComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		dashboardComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		for (DashboardPartPlacement dashboardPartPlace : dashboardParts) {
			Composite outerContainer = createDashboardPartOuterContainer(dashboardComposite, dashboardPartPlace);

			createContainerDecoration(outerContainer, dashboardPartPlace);

			Composite innerContainer = getDashboardPartInnerContainer(outerContainer);

			dashboardPartPlace.getDashboardPart().createPartControl(innerContainer, toolBar);

			innerContainer.setToolTipText(dashboardPartPlace.getTitle());

		}
	}

	private static void createContainerDecoration(Composite outerContainer, DashboardPartPlacement dashboardPartPlace) {
		if( dashboardPartPlace.hasTitle()) {
			Label titleLabel = new Label(outerContainer, SWT.NONE);
			titleLabel.setText(dashboardPartPlace.getTitle());
			titleLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}
	}

	private static Composite createDashboardPartOuterContainer(Composite dashboardComposite, DashboardPartPlacement dashboardPartPlace) {
		Composite container = new Composite(dashboardComposite, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginBottom = 0;
		layout.marginHeight = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginTop = 0;
		layout.marginWidth = 0;
		container.setLayout(layout);

		FormData fd = new FormData();
		fd.height = dashboardPartPlace.getHeight();
		fd.width = dashboardPartPlace.getWidth();
		fd.top = new FormAttachment(0, dashboardPartPlace.getY());
		fd.left = new FormAttachment(0, dashboardPartPlace.getX());
		fd.bottom = new FormAttachment(0, dashboardPartPlace.getY() + fd.height);
		fd.right = new FormAttachment(0, dashboardPartPlace.getX() + fd.width);
		container.setLayoutData(fd);
		return container;
	}

	private static Composite getDashboardPartInnerContainer(Composite container) {
		Composite containerDummy = new Composite(container, SWT.NONE);
		containerDummy.setLayout(new GridLayout());
		containerDummy.setLayoutData(new GridData(GridData.FILL_BOTH));
		containerDummy.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		return containerDummy;
	}
}
