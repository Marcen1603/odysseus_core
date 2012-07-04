package de.uniol.inf.is.odysseus.rcp.dashboard.part;

import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;

public class TableDashboardPart extends AbstractDashboardPart {

	private IPhysicalOperator operator;

	private TableViewer tableViewer;
	
	private String[] attributes;
	private int[] positions;
	
	private List<Tuple<?>> data = Lists.newArrayList();
	
	@Override
	public void createPartControl(Composite parent) {
		
		String attributeList = getConfiguration().get("Attributes");
		if (Strings.isNullOrEmpty(attributeList)) {
			attributeList = "*";
		}

		attributes = attributeList.split(",");

		int colCount = attributes.length;
		
		Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		tableViewer = new TableViewer(tableComposite, SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		for( int i = 0; i < colCount; i++ ) {
			
			final int finalI = i;
			
			TableViewerColumn fileNameColumn = new TableViewerColumn(tableViewer, SWT.NONE);
			fileNameColumn.getColumn().setText(attributes[i]);
			tableColumnLayout.setColumnData(fileNameColumn.getColumn(), new ColumnWeightData(5, 25, true));
			fileNameColumn.setLabelProvider(new CellLabelProvider() {
				@Override
				public void update(ViewerCell cell) {
					Tuple<?> tuple = (Tuple<?>)cell.getElement();
					Object attrValue = tuple.getAttributes()[positions[finalI]];
					cell.setText(attrValue != null ? attrValue.toString() : "null");
				}
			});
		}
		
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setInput(data);
		
	}
	
	@Override
	public void onStart(List<IPhysicalOperator> physicalRoots) throws Exception {
		super.onStart(physicalRoots);

		if (physicalRoots.size() > 1) {
			throw new Exception("Table DashboardPart only supports one query!");
		}

		operator = physicalRoots.get(0);
		positions = determinePositions(operator.getOutputSchema(), attributes);

		System.out.println(attributes);
		System.out.println(operator.getOutputSchema());
	}

	@Override
	public void streamElementRecieved(final Object element, int port) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				data.add((Tuple<?>)element);
				if( data.size() > 10 ) {
					data.remove(0);
				}
				
				if( !tableViewer.getTable().isDisposed()) {
					tableViewer.refresh();
				}
				
			}
		});
	}

	@Override
	public void punctuationElementRecieved(PointInTime point, int port) {

	}

	@Override
	public void settingChanged(String settingName, Object oldValue, Object newValue) {

	}

	private static int[] determinePositions(SDFSchema outputSchema, String[] attributes2) {
		int[] positions = new int[attributes2.length];
		
		for( int i = 0 ; i < attributes2.length; i++ ) {
			for( int j = 0; j < outputSchema.size(); j++ ) {
				if( outputSchema.get(j).getAttributeName().equals(attributes2[i])) {
					positions[i] = j;
					break;
				}
			}
		}
		
		return positions;
	}
}
