package de.uniol.inf.is.odysseus.viewer.swt.diagram;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import de.uniol.inf.is.odysseus.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.viewer.view.stream.Parameters;

public class SWTListDiagram extends AbstractSWTDiagram<String[]> {
	
	private Table table;
	private boolean columnsAdded = false;
	private final int maxItems;
	private Collection<TableColumn> columns = new ArrayList<TableColumn>();
	
	public SWTListDiagram( INodeModel<?> node, Composite composite, Parameters params ) {
		super( node, composite, params );
		
		composite.setLayout( new GridLayout() );
		table = new Table( composite, SWT.MULTI | SWT.BORDER );
		table.setLinesVisible( true );
		table.setHeaderVisible( true );
		GridData tableGridData = new GridData( GridData.FILL_BOTH );
		table.setLayoutData( tableGridData );
		
		table.addListener( SWT.Resize, new Listener() {
			@Override
			public void handleEvent( Event arg0 ) {
				if( columns.size() > 0 ) {
					final int width = table.getClientArea().width;
					final int colWidth = width / columns.size();
					
					for( TableColumn col : columns )
						col.setWidth( colWidth );
				}
			}
		});
		
		maxItems = params.getInteger( "maxItems", -1 );
	}
	
	@Override
	public void reset() {
		table.clearAll();
		table.setItemCount( 0 );
	}

	@Override
	public void dataElementRecievedSynchronized( String[] element, int port ) {
		if( table.isDisposed() )
			return;
		
		if( columnsAdded == false ) {
			for(int i = 0; i < element.length; i++ ) {
				TableColumn col = new TableColumn( table, SWT.NONE);
				col.setText( String.valueOf(i) );
				col.setAlignment( SWT.CENTER );
				col.pack();
				columns.add(col);
			}
			
			columnsAdded = true;
		}
		
		TableItem ti = new TableItem( table, 0 );
		for(int j = 0; j < element.length; j++ ) {
			ti.setText( j, element[j] );
		}
		
		if( maxItems > 0 && table.getItemCount() > maxItems )
			table.remove( 0 );
	}

}
