package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;

import com.google.common.base.Preconditions;

public class DashboardPartControl {

	private final Composite parent;
	private final Composite outerComposite;
	private final DashboardPartPlacement dashboardPartPlacement;

	public DashboardPartControl(Composite parent, ToolBar toolBar, DashboardPartPlacement dashboardPartPlace) {
		Preconditions.checkNotNull(parent, "Parent for dashboard part control must not be null!");
		Preconditions.checkNotNull(toolBar, "ToolBar for dashboard part control must not be null!");
		Preconditions.checkNotNull(dashboardPartPlace, "Placement for dashboard part control must not be null!");

		this.parent = parent;
		dashboardPartPlacement = dashboardPartPlace;

		outerComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginBottom = 0;
		layout.marginHeight = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginTop = 0;
		layout.marginWidth = 0;
		outerComposite.setLayout(layout);

		FormData fd = new FormData();
		updateFormData(fd, dashboardPartPlacement);
		outerComposite.setLayoutData(fd);
		
		Composite innerComposite = new Composite(outerComposite, SWT.NONE);
		innerComposite.setLayout(new GridLayout());
		innerComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		innerComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		dashboardPartPlace.getDashboardPart().createPartControl(innerComposite, toolBar);
	}
	
	public void dispose() {
		outerComposite.dispose();
	}
	
	public void update() {
		final FormData fd = (FormData) outerComposite.getLayoutData();
		updateFormData(fd, dashboardPartPlacement);
		
		outerComposite.redraw();
		outerComposite.layout();
		
		parent.layout();
		parent.redraw();
	}

	private static void updateFormData(FormData fd, DashboardPartPlacement dashboardPartPlace) {
		fd.height = dashboardPartPlace.getHeight();
		fd.width = dashboardPartPlace.getWidth();
		fd.top = new FormAttachment(0, dashboardPartPlace.getY());
		fd.left = new FormAttachment(0, dashboardPartPlace.getX());
		fd.bottom = new FormAttachment(0, dashboardPartPlace.getY() + fd.height);
		fd.right = new FormAttachment(0, dashboardPartPlace.getX() + fd.width);
	}

	public Composite getComposite() {
		return outerComposite;
	}
}
