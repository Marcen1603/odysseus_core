package de.uniol.inf.is.odysseus.rcp.viewer.stream.table;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;

import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.editor.StreamEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamEditorType;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class StreamTableEditor implements IStreamEditorType {

	private IEditorPart part;
	private TableViewer viewer;
	private SDFAttributeList schema;
	
	private List<RelationalTuple<?>> tuples = new ArrayList<RelationalTuple<?>>();
	private int maxTuples;
	
	public StreamTableEditor(int maxTuples) {
		this.maxTuples = maxTuples > 0 ? maxTuples : Integer.MAX_VALUE;
	}

	@Override
	public void streamElementRecieved(Object element, int port) {
		if( !(element instanceof RelationalTuple<?>)) {
			System.out.println("Warning: StreamTable is only for relational tuple!");
			return;
		}
			
		tuples.add(0, (RelationalTuple<?>)element);
		if( tuples.size() > maxTuples ) {
			tuples.remove(tuples.size()-1);
		}
		part.getSite().getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				viewer.refresh();
			}
		});
	}

	@Override
	public void init(StreamEditor editorPart, IStreamEditorInput editorInput) {
		ISource<?>[] sources = editorInput.getStreamConnection().getSources().toArray(new ISource<?>[0]);
		schema = sources[0].getOutputSchema();
		part = editorPart;
		
	}

	@Override
	public void createPartControl(Composite parent) {
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		parent.setLayout(tableColumnLayout);
		
		viewer = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		
		final int weight = 1000 / schema.getAttributeCount();
		for( int i = 0; i < schema.getAttributeCount(); i++ ) {
			SDFAttribute attribute = schema.getAttribute(i);
			
			TableViewerColumn col = new TableViewerColumn(viewer, SWT.NONE);
			col.getColumn().setText(attribute.getAttributeName());
			col.getColumn().setAlignment(SWT.CENTER);
			final int fi = i;
			col.setLabelProvider( new CellLabelProvider() {
				@Override
				public void update(ViewerCell cell) {
					cell.setText( ((RelationalTuple<?>)cell.getElement()).getAttribute(fi).toString() );
				}
			});
			tableColumnLayout.setColumnData(col.getColumn(), new ColumnWeightData(weight,25,true));
		}
		
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setInput(tuples);
		
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void dispose() {}

}
