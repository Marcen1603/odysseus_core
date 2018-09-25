package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class DialogUtils {

	public static GridLayout getMainLayout() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.horizontalSpacing = GridData.BEGINNING;
		layout.verticalSpacing = GridData.BEGINNING;
		return layout;
	}

	public static GridLayout getGroupLayout() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = GridData.FILL;
		layout.verticalSpacing = GridData.BEGINNING;
		return layout;
	}

	public static GridData getLabelDataLayout() {
		GridData gridLabelLayout = new GridData();
		gridLabelLayout.widthHint = 150;
		gridLabelLayout.heightHint = 25;
		return gridLabelLayout;
	}

	public static GridData getTextDataLayout() {
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.heightHint = 20;
		return gridData;
	}


	public static void separator(Composite parent) {
		GridData separatorgridData = new GridData();
		separatorgridData.horizontalAlignment = GridData.FILL;
		separatorgridData.grabExcessHorizontalSpace = true;
		separatorgridData.grabExcessVerticalSpace = false;
		separatorgridData.horizontalSpan = 2;

		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(separatorgridData);
	}
	
	public static Composite getFlexArea(Composite parent) {
		GridLayout freespaceLayout = getGroupLayout();
		freespaceLayout.numColumns = 2;
		freespaceLayout.horizontalSpacing = GridData.FILL;
		freespaceLayout.verticalSpacing = GridData.FILL;

		final Composite freeSpace = new Composite(parent, SWT.NONE);
		freeSpace.setLayout(freespaceLayout);
		freeSpace.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		return freeSpace;
	}
	
}
