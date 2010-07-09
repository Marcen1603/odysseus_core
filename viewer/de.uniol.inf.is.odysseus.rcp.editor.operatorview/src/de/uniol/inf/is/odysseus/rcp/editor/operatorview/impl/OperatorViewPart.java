package de.uniol.inf.is.odysseus.rcp.editor.operatorview.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.rcp.editor.operator.IOperatorExtensionDescriptor;
import de.uniol.inf.is.odysseus.rcp.editor.operator.OperatorExtensionRegistry;

public class OperatorViewPart extends ViewPart {

	private TreeViewer treeViewer;
	
	public OperatorViewPart() {
	}

	@Override
	public void createPartControl(Composite parent) {
		Map<String, OperatorGroup> groups = new HashMap<String, OperatorGroup>();
		Collection<OperatorGroup> groupsCol = new ArrayList<OperatorGroup>();
		Collection<String> groupNames = OperatorExtensionRegistry.getInstance().getGroups();
		for( String grp : groupNames ) {
			OperatorGroup group = new OperatorGroup(grp);
			Collection<IOperatorExtensionDescriptor> descs = OperatorExtensionRegistry.getInstance().getExtensions().getGroup(grp);
			for( IOperatorExtensionDescriptor desc : descs ) 
				group.addExtension(desc);
			groups.put(grp, group);
			groupsCol.add(group);
		}
		
		treeViewer = new TreeViewer(parent, SWT.SINGLE);

		int operations = DND.DROP_COPY;
		Transfer[] transferTypes = new Transfer[]{TextTransfer.getInstance()};
		treeViewer.addDragSupport(operations, transferTypes , new DragSourceListener() {

			@Override
			public void dragStart(DragSourceEvent event) {
				IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
				Object firstElement = selection.getFirstElement();
				
				if( !(firstElement instanceof IOperatorExtensionDescriptor) ) {
					event.doit = false;
				}
			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
				Object firstElement = selection.getFirstElement();
				
				if( TextTransfer.getInstance().isSupportedType(event.dataType) ) {
					event.data = ((IOperatorExtensionDescriptor)firstElement).getID();
				}
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
			}
			
		});
		
		treeViewer.setContentProvider( new OperatorViewContentProvider(groups));
		treeViewer.setLabelProvider( new OperatorViewLabelProvider());
		
		treeViewer.setInput(groupsCol);
		
		getSite().setSelectionProvider(treeViewer);
	}

	@Override
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}

}
