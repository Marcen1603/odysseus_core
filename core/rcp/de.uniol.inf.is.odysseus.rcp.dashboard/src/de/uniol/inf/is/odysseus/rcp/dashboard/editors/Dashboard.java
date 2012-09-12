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
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public final class Dashboard implements PaintListener, MouseListener, KeyListener {

	private static final Logger LOG = LoggerFactory.getLogger(Dashboard.class);
	
	// 1 = LMB, 2 = MMB, 3 = RMB
	private static final int SELECT_MOUSE_BUTTON_ID = 1;
	private static final int SELECTION_BORDER_MARGIN = 3;
	private static final int MOVE_SELECTION_STEP_SIZE_PIXELS = 10;
	private static final int RESIZE_SELECTION_STEP_SIZE_PIXELS = 10;

	private Composite dashboardComposite;
	private ToolBar toolBar;
	private Composite parent;

	private List<DashboardPartPlacement> dashboardParts = Lists.newArrayList();
	private DashboardPartPlacement selectedDashboardPart;
	private Map<Control, DashboardPartPlacement> controlsMap = Maps.newHashMap();
	private Map<DashboardPartPlacement, Composite> containers = Maps.newHashMap();
	
	private List<IDashboardListener> listeners = Lists.newArrayList();

	public void createPartControl(Composite parent, ToolBar toolBar) {
		this.toolBar = toolBar;
		this.parent = parent;
		
		dashboardComposite = new Composite(parent, SWT.BORDER);
		dashboardComposite.setLayout(new FormLayout());
		dashboardComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		dashboardComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		
		dashboardComposite.addMouseListener(this);
		dashboardComposite.addKeyListener(this);
		dashboardComposite.addPaintListener(this);

		for (DashboardPartPlacement dashboardPartPlace : dashboardParts) {
			insertDashboardPart(dashboardPartPlace);
		}
		
		parent.layout();
	}
	
	public void add(DashboardPartPlacement partPlace) {
		Preconditions.checkNotNull(partPlace, "Placement for Dashboard Part must not be null!");
		Preconditions.checkArgument(!dashboardParts.contains(partPlace), "Dashboard part placement %s already added!", partPlace);

		dashboardParts.add(partPlace);
		
		if( dashboardComposite != null && toolBar != null ) {
			insertDashboardPart(partPlace);
			dashboardComposite.layout();
		}
	}

	public void remove(DashboardPartPlacement partPlace) {
		deletePartControl();		
		dashboardParts.remove(partPlace);
		
		createPartControl(parent, toolBar);
		
		fireChangedEvent();
	}

	
	public void addListener( IDashboardListener listener ) {
		Preconditions.checkNotNull(listener, "Dashboardlistener to add must not be null!");
		
		synchronized(listeners ) {
			listeners.add(listener);
		}
	}
	
	public void removeListener( IDashboardListener listener ) {
		synchronized(listeners ) {
			listeners.remove(listener);
		}
	}
	
	public ImmutableList<DashboardPartPlacement> getDashboardPartPlacements() {
		return ImmutableList.copyOf(dashboardParts);
	}

	@Override
	public void paintControl(PaintEvent e) {
		renderSelectionBorder();
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseDown(MouseEvent e) {
		if (e.button == SELECT_MOUSE_BUTTON_ID) {
			selectedDashboardPart = controlsMap.get(e.widget);
			dashboardComposite.redraw();
		}
	}

	@Override
	public void mouseUp(MouseEvent e) {
		// do nothing
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (hasSelection() && (e.stateMask & SWT.CTRL) != 0) {

			if( e.keyCode == SWT.DEL) {
				remove(selectedDashboardPart);
				selectedDashboardPart = null;

			} else if( ( e.stateMask & SWT.SHIFT ) != 0 ) {
				if (e.keyCode == SWT.ARROW_UP) {
					selectedDashboardPart.setHeight(selectedDashboardPart.getHeight() - RESIZE_SELECTION_STEP_SIZE_PIXELS);
					updateSelection();
	
				} else if (e.keyCode == SWT.ARROW_DOWN) {
					selectedDashboardPart.setHeight(selectedDashboardPart.getHeight() + RESIZE_SELECTION_STEP_SIZE_PIXELS);
					updateSelection();
	
				} else if (e.keyCode == SWT.ARROW_LEFT) {
					selectedDashboardPart.setWidth(selectedDashboardPart.getWidth() - RESIZE_SELECTION_STEP_SIZE_PIXELS);
					updateSelection();
	
				} else if (e.keyCode == SWT.ARROW_RIGHT) {
					selectedDashboardPart.setWidth(selectedDashboardPart.getWidth() + RESIZE_SELECTION_STEP_SIZE_PIXELS);
					updateSelection();
	
				} else if (e.keyCode == SWT.TAB) {
					int pos = dashboardParts.indexOf(selectedDashboardPart);
					int newPos = (pos + 1) % dashboardParts.size();
					selectedDashboardPart = dashboardParts.get(newPos);
					dashboardComposite.redraw();
					fireChangedEvent();
				}
			} else {
				if (e.keyCode == SWT.ARROW_UP) {
					selectedDashboardPart.setY(selectedDashboardPart.getY() - MOVE_SELECTION_STEP_SIZE_PIXELS);
					updateSelection();
	
				} else if (e.keyCode == SWT.ARROW_DOWN) {
					selectedDashboardPart.setY(selectedDashboardPart.getY() + MOVE_SELECTION_STEP_SIZE_PIXELS);
					updateSelection();
	
				} else if (e.keyCode == SWT.ARROW_LEFT) {
					selectedDashboardPart.setX(selectedDashboardPart.getX() - MOVE_SELECTION_STEP_SIZE_PIXELS);
					updateSelection();
	
				} else if (e.keyCode == SWT.ARROW_RIGHT) {
					selectedDashboardPart.setX(selectedDashboardPart.getX() + MOVE_SELECTION_STEP_SIZE_PIXELS);
					updateSelection();
	
				} else if (e.keyCode == SWT.TAB) {
					int pos = dashboardParts.indexOf(selectedDashboardPart);
					int newPos = (pos + 1) % dashboardParts.size();
					selectedDashboardPart = dashboardParts.get(newPos);
					dashboardComposite.redraw();
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// do nothing
	}

	private void deletePartControl() {
		removeListeners(dashboardComposite);
		dashboardComposite.dispose();
		dashboardComposite = null;
	}

	private void insertDashboardPart(DashboardPartPlacement dashboardPartPlace) {
		Composite outerContainer = createDashboardPartOuterContainer(dashboardComposite, dashboardPartPlace);
		containers.put(dashboardPartPlace, outerContainer);

		createContainerDecoration(outerContainer, dashboardPartPlace);
		Composite innerContainer = getDashboardPartInnerContainer(outerContainer);

		dashboardPartPlace.getDashboardPart().createPartControl(innerContainer, toolBar);
		innerContainer.setToolTipText(dashboardPartPlace.getTitle());

		addControlsToMap(outerContainer, dashboardPartPlace, controlsMap);
		
		addListeners(outerContainer);
	}
	
	private void fireChangedEvent() {
		synchronized( listeners ) {
			for( IDashboardListener listener : listeners ) {
				try {
					listener.dashboardChanged(this);
				} catch( Throwable t ) {
					LOG.error("Exception during executing DashboardListener", t);
				}
			}
		}
	}

	private void addListeners(Control base) {
		base.addMouseListener(this);
		base.addKeyListener(this);
		if (base instanceof Composite) {
			for (Control ctrl : ((Composite) base).getChildren()) {
				addListeners(ctrl);
			}
		}
	}
	
	private void removeListeners(Control base) {
		base.removeMouseListener(this);
		base.removeKeyListener(this);
		if (base instanceof Composite) {
			for (Control ctrl : ((Composite) base).getChildren()) {
				addListeners(ctrl);
			}
		}
	}

	private void renderSelectionBorder() {
		if (hasSelection()) {
			int x = selectedDashboardPart.getX();
			int y = selectedDashboardPart.getY();
			int w = selectedDashboardPart.getWidth();
			int h = selectedDashboardPart.getHeight();

			GC gc = new GC(dashboardComposite);
			gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			gc.setLineWidth(3);
			gc.drawRectangle(x - SELECTION_BORDER_MARGIN, y - SELECTION_BORDER_MARGIN, w + SELECTION_BORDER_MARGIN * 2, h + SELECTION_BORDER_MARGIN * 2);
		}
	}

	private boolean hasSelection() {
		return selectedDashboardPart != null;
	}

	private void updateSelection() {
		Composite comp = containers.get(selectedDashboardPart);
		FormData fd = (FormData) comp.getLayoutData();
		updateFormData(fd, selectedDashboardPart);
		dashboardComposite.layout();
		dashboardComposite.redraw();
		fireChangedEvent();
	}
	
	private static void createContainerDecoration(Composite outerContainer, DashboardPartPlacement dashboardPartPlace) {
		if (dashboardPartPlace.hasTitle()) {
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
		updateFormData(fd, dashboardPartPlace);
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

	private static void addControlsToMap(Control innerContainer, DashboardPartPlacement placement, Map<Control, DashboardPartPlacement> controlsMap) {
		controlsMap.put(innerContainer, placement);

		if (innerContainer instanceof Composite) {
			Composite comp = (Composite) innerContainer;
			for (Control compControl : comp.getChildren()) {
				addControlsToMap(compControl, placement, controlsMap);
			}
		}
	}

	private static void updateFormData(FormData fd, DashboardPartPlacement dashboardPartPlace) {
		fd.height = dashboardPartPlace.getHeight();
		fd.width = dashboardPartPlace.getWidth();
		fd.top = new FormAttachment(0, dashboardPartPlace.getY());
		fd.left = new FormAttachment(0, dashboardPartPlace.getX());
		fd.bottom = new FormAttachment(0, dashboardPartPlace.getY() + fd.height);
		fd.right = new FormAttachment(0, dashboardPartPlace.getX() + fd.width);
	}
}
