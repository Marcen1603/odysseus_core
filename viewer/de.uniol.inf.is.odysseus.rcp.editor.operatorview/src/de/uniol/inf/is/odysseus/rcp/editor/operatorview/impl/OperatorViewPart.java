package de.uniol.inf.is.odysseus.rcp.editor.operatorview.impl;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.rcp.editor.model.cfg.OperatorDescriptorRegistry;

public class OperatorViewPart extends ViewPart {

	private TableViewer tableViewer;
	
	public OperatorViewPart() {
	}

	@Override
	public void createPartControl(Composite parent) {
		tableViewer = new TableViewer(parent, SWT.SINGLE);
		
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setLabelProvider( new OperatorViewLabelProvider());
		
		tableViewer.setInput(OperatorDescriptorRegistry.getInstance().getAll());
		
		getSite().setSelectionProvider(tableViewer);
	}

	@Override
	public void setFocus() {
		tableViewer.getControl().setFocus();
	}

}
