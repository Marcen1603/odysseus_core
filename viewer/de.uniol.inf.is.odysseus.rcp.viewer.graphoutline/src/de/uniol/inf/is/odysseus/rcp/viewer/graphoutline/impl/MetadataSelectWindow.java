package de.uniol.inf.is.odysseus.rcp.viewer.graphoutline.impl;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.rcp.viewer.model.Model;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusNodeModel;

public class MetadataSelectWindow {

	public MetadataSelectWindow( Shell baseWindow, final IOdysseusNodeModel nodeModel ) {
		final Shell wnd = new Shell( baseWindow, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL ); // erstellt Dialog
		wnd.setText( "Metadatenauswahl" );
		wnd.setSize(  200, 400  );
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		wnd.setLayout( gridLayout );
		
		// Liste der möglichen Metadatentypen
		final List list = new List( wnd, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		GridData data = new GridData( GridData.FILL_BOTH );
		data.horizontalSpan = 2;
		list.setLayoutData( data );
		
		// Buttons
		Button okButton = new Button( wnd, SWT.PUSH );
		data = new GridData( GridData.FILL_HORIZONTAL );
		data.horizontalSpan = 1;
		okButton.setLayoutData( data );
		okButton.setText( "OK" );
		okButton.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent e ) {
				String[] selected = list.getSelection();
				for( String itemType : selected ) {
					if( nodeModel.getProvidedMetadataTypes().contains( itemType ))
						continue;
					
					IMonitoringData<?> item = Model.getInstance().getMetadataProvider().createMetadata( itemType, nodeModel );
					nodeModel.addMetadataItem( item.getType(), item );
				}
				wnd.close();
				wnd.dispose();
			}
		});
		
		Button cancelButton = new Button( wnd, SWT.PUSH );
		cancelButton.setText( "Abbrechen" );
		cancelButton.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent e ) {
				wnd.close();
				wnd.dispose();
			}
		});
		data = new GridData( GridData.FILL_HORIZONTAL );
		data.horizontalSpan = 1;
		cancelButton.setLayoutData( data );
		
		// Liste mit Inhalt füllen 
		// Nur die Metadatentypen zur Auswahl stellen, welche noch nicht eingesetzt wurden
		Collection<String> items = Model.getInstance().getMetadataProvider().getList();
		Collection<String> alreadyRegisteredItems = nodeModel.getProvidedMetadataTypes();
		for( String i : items ) 
			if( !alreadyRegisteredItems.contains( i ) )
				list.add( i );
		
		wnd.open();
		
	}
}
