package de.uniol.inf.is.odysseus.viewer.swt;

import java.util.Collection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.viewer.Activator;
import de.uniol.inf.is.odysseus.viewer.view.graph.IOdysseusNodeView;
import de.uniol.inf.is.odysseus.viewer.view.stream.DiagramInfo;
import de.uniol.inf.is.odysseus.viewer.view.stream.IDiagramConfiguration;
import de.uniol.inf.is.odysseus.viewer.view.stream.XMLDiagramConfiguration;

public class SWTDiagramSelectWindow {

//	private static final Logger logger = LoggerFactory.getLogger( SWTDiagramSelectWindow.class );
	
	public SWTDiagramSelectWindow( Shell baseWindow, final IOdysseusNodeView node ) {
		
		final IDiagramConfiguration diagramConfig = new XMLDiagramConfiguration( Activator.DIAGRAM_CFG_FILE, Activator.XSD_DIAGRAMM_SCHEMA_FILE );
			
		final Shell wnd = new Shell( baseWindow, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL ); // erstellt Dialog
		wnd.setText( "Diagrammauswahl" );
		wnd.setSize(  200, 400  );
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		wnd.setLayout( gridLayout );
		
		// Liste der möglichen Diagrammtypen
		final List list = new List( wnd, SWT.BORDER | SWT.V_SCROLL );
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
			
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected( SelectionEvent e ) {
				String[] selected = list.getSelection();
				if( selected != null && selected.length != 0 ) {
					
					// Fenster erstellen
					for( String select : selected ) {
						
						// Diagrammkonfiguration raussuchen
						for( DiagramInfo i : diagramConfig.getDiagramInfo()) {
						
							if( i.getName().equals( select )) {
								final Shell streamViewShell = new Shell( wnd.getDisplay() );
								streamViewShell.setSize( 400, 200 );
						
								new SWTStreamWindow(streamViewShell, node, i);
								streamViewShell.open();
							}
						}
					}
					wnd.close();
					wnd.dispose();
				}
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
		Collection<DiagramInfo> items = diagramConfig.getDiagramInfo();
		for( DiagramInfo i : items ) 
			list.add( i.getName() );
		
		wnd.open();
		
	}
}
