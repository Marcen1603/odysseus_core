package de.uniol.inf.is.odysseus.rcp.viewer.nodeview;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.rcp.resource.ResourceManager;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModelChangeListener;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusNodeModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.meta.IMetadataChangeListener;
import de.uniol.inf.is.odysseus.rcp.viewer.model.meta.IMetadataModel;


public class NodeInfoPanel implements INodeModelChangeListener<IPhysicalOperator>, IMetadataChangeListener<IMonitoringData<?>> {

	private static final Logger logger = LoggerFactory.getLogger( NodeInfoPanel.class );
	
	private final Composite composite;
	
	private final Table table;
	private final TableColumn valueCol;
	private final TableColumn nameCol;
	
	private final IOdysseusNodeModel node;
	private final Text nameText;
	private final Label endGradeLabel;
	private final Label startGradeLabel;
	
	private final Button checkDataButton;
	
	private boolean pinned;
	
	// Workaround für das Ausblenden
	private int oldWidth;
	private int oldHeight;
	
	public NodeInfoPanel( final Composite parent, final IOdysseusNodeModel nodeToShow ) {
				
		if( parent == null ) 
			throw new IllegalArgumentException("parent is null!");
		
		if( nodeToShow == null ) 
			throw new IllegalArgumentException("nodeToShow is null!");
		
		node = nodeToShow;
		if( node == null ) 
			throw new IllegalArgumentException("nodeToShow is null!");
		
		// Struktur
		composite = new Composite(parent, SWT.BORDER);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.verticalSpacing = 3;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		composite.setLayout( gridLayout );
		GridData data = new GridData( GridData.FILL_HORIZONTAL);
		composite.setLayoutData( data );
				
		nameText = new Text( composite, SWT.SINGLE );
		nameText.setText( node.getName());
		GridData data1 = new GridData(GridData.FILL_HORIZONTAL);
		data1.horizontalSpan = 2;
		nameText.setLayoutData( data1 );
		nameText.addModifyListener( new ModifyListener() {

			@Override
			public void modifyText( ModifyEvent arg0 ) {
				final String newName = nameText.getText();
				if( newName != null && newName.length() > 0 )
					node.setName(newName);
			}
			
		});
		
		nameText.addFocusListener( new FocusListener() {
			@Override
			public void focusLost( FocusEvent arg0 ) {
				final String newName = ((Text)arg0.widget).getText();
				if( newName != null && newName.length() > 0 )
					node.setName(newName);
			}
			@Override
			public void focusGained( FocusEvent arg0 ) {}
		});
		
		Composite buttonList = new Composite( composite, 0 );
		data1 = new GridData();
		data1.horizontalAlignment = GridData.END;
		buttonList.setLayoutData( data1 );
		gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		buttonList.setLayout( gridLayout );
		
		// Sichtbarkeitsbutton
		final Button visibleButton = new Button( buttonList, SWT.TOGGLE );
		visibleButton.setImage( ResourceManager.getInstance().getImage( "hide" ) );
		visibleButton.setToolTipText( "Shrink" );
		
		// Pinbutton
		final Button pinButton = new Button( buttonList, SWT.TOGGLE );
		pinButton.setImage( ResourceManager.getInstance().getImage( "pin" ) );
		pinButton.setToolTipText( "Not fixed" );
		pinButton.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent e ) {
				pinned = pinButton.getSelection();
				if( pinned ) {
					pinButton.setToolTipText( "Fixed" );
					pinButton.setImage( ResourceManager.getInstance().getImage( "pinned" ) );
				} else {
					pinButton.setToolTipText( "Not fixed" );
					pinButton.setImage( ResourceManager.getInstance().getImage( "pin" ) );
				}
			}
		});
		
		final Button selectButton = new Button( buttonList, SWT.PUSH );
		selectButton.setImage( ResourceManager.getInstance().getImage( "select" ));
		selectButton.setToolTipText( "Select node in editor" );
		selectButton.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent arg0 ) {
				// knoten im aktiven editor selektieren
			}
		});
		
		// Inhalt
		final Composite content = new Composite( composite, 0 );
		final GridData contentGridData = new GridData(GridData.FILL_HORIZONTAL);
		contentGridData.horizontalSpan = 3;
		content.setLayoutData( contentGridData );
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		content.setLayout( gridLayout );
		visibleButton.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent e ) {
				if( !visibleButton.getSelection() ) {
					content.setVisible( true );
					contentGridData.widthHint = oldWidth;
					contentGridData.heightHint = oldHeight;
					visibleButton.setImage(ResourceManager.getInstance().getImage( "hide" ) );
					visibleButton.setToolTipText( "Shrink" );
					parent.layout();
				} else {
					content.setVisible( false );
					oldWidth = content.getSize().x;
					oldHeight = content.getSize().y;
					contentGridData.widthHint = 0;
					contentGridData.heightHint = 0;
					visibleButton.setImage(ResourceManager.getInstance().getImage( "show" ) );
					visibleButton.setToolTipText( "Grow" );
					parent.layout();
				}
			}
		});
		
		// Operatortyp-anzeige
		if( nodeToShow.getContent() != null ) {
			Label operatorName = new Label(content, SWT.SHADOW_IN);
			operatorName.setText( "Operatortype: " + nodeToShow.getContent().getClass().getSimpleName() );
			data1 = new GridData( GridData.FILL_HORIZONTAL );
			data1.horizontalSpan = 2;
			operatorName.setLayoutData( data1 );
		}
				
		// Nachbarschaftszeile
		endGradeLabel = new Label(content, SWT.SHADOW_IN);
		endGradeLabel.setText( "#Parents:" + node.getConnectionsAsEndNode().size()  );
		data1 = new GridData( GridData.FILL_HORIZONTAL );
		data1.horizontalSpan = 2;
		endGradeLabel.setLayoutData( data1 );
		
		startGradeLabel = new Label(content, SWT.SHADOW_IN);
		startGradeLabel.setText( "#Childs:" + node.getConnectionsAsStartNode().size()   );
		GridData gradeLabelData2 = new GridData(GridData.FILL_HORIZONTAL);
		gradeLabelData2.horizontalSpan = 2;
		startGradeLabel.setLayoutData( gradeLabelData2 );
		

		// Tabelle
		table = new Table(content, SWT.MULTI | SWT.BORDER);
		table.setLinesVisible( true );
		table.setHeaderVisible( true );
		GridData tableGridData = new GridData( GridData.FILL_HORIZONTAL );
		tableGridData.horizontalSpan = 2;
		table.setLayoutData( tableGridData );
		
		nameCol = new TableColumn(table, SWT.NONE);
		nameCol.setText( "Metadatatype" );
		
		valueCol = new TableColumn(table, SWT.NONE);
		valueCol.setText( "Value" );
		fillTable();
		
		Composite tableEditContent = new Composite( content, 0 );
		GridLayout gl = new GridLayout();
		gl.numColumns = 4;
		tableEditContent.setLayout( gl  );
		GridData tabelEditGridData = new GridData();
		tabelEditGridData.horizontalSpan = 2;
		tableEditContent.setLayoutData( tabelEditGridData );
		
		Button addMetadataButton = new Button( tableEditContent, SWT.PUSH );
		addMetadataButton.setImage( ResourceManager.getInstance().getImage( "metaNew" ) );
		addMetadataButton.setToolTipText( "Add metadata" );
		addMetadataButton.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent e ) {
				// Dialog öffnen
				new MetadataSelectWindow(parent.getShell(), node );
			}
		});
		
		Button removeMetadataButton = new Button( tableEditContent, SWT.PUSH );
		removeMetadataButton.setImage( ResourceManager.getInstance().getImage( "metaRemove" ) );
		removeMetadataButton.setToolTipText( "Remove metadata" );
		removeMetadataButton.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent e ) {
				TableItem[] selectedItems = table.getSelection();
				if( selectedItems.length > 0 ) {
					MessageBox msgBox = new MessageBox( parent.getShell(), SWT.YES | SWT.NO | SWT.ICON_QUESTION );
					msgBox.setText( "Warning" );
					msgBox.setMessage( "Are you sure to remove " + selectedItems.length + " metadata?" );
					if( msgBox.open() == SWT.YES ) {
						for( TableItem item : selectedItems ) {
							if( nodeToShow != null ) {
								nodeToShow.removeMetadataItem( item.getText(0) );
							}
						}
					}
				}
			}
		});
		
		Button resetMetadataButton = new Button( tableEditContent, SWT.PUSH );
		resetMetadataButton.setImage( ResourceManager.getInstance().getImage( "metaReset" ) );
		resetMetadataButton.setToolTipText( "Reset metadata" );
		resetMetadataButton.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent e ) {
				Collection<String> selectedTypes = getSelectedMetadataTypes();
				for( String metaType : selectedTypes ) {
					if( nodeToShow != null ) {
						nodeToShow.resetMetadataItem( metaType );
					}
				}
			}
		});
		
		Button refreshMetadataButton = new Button( tableEditContent, SWT.PUSH );
		refreshMetadataButton.setImage( ResourceManager.getInstance().getImage( "metaReload" ) );
		refreshMetadataButton.setToolTipText( "Update metadata" );
		refreshMetadataButton.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent arg0 ) {
				fillTable();
			}
		});
		
		// Button zum Beobachten von Daten
		checkDataButton = new Button(content, SWT.PUSH);
		checkDataButton.setText( "View datastream" );
		GridData checkDataGridData = new GridData(GridData.FILL_HORIZONTAL);
		checkDataGridData.horizontalSpan = 2;
		checkDataButton.setLayoutData( checkDataGridData );
		checkDataButton.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent e ) {
				// show datastream
			}
		});
				
		logger.debug( "NodeInfoPanel for node '" + node + "' created." );
		
		node.addNodeModelChangeListener( this );
		node.addMetadataChangeListener( this );
	}
		
	public final Composite getComposite() {
		return composite;
	}
		
	public void dispose() {
		composite.dispose();
		
		node.removeNodeModelChangeListener( this );
		node.removeMetadataChangeListener( this );
		
		logger.debug( "NodeInfoPanel for node '" + node + "' disposed." );
	}
	
	public boolean isPinned() {
		return pinned;
	}
	
	private void fillTable() {
		table.clearAll();
		table.setItemCount( 0 );
		
		// Inhalt der Tabelle
		Collection<String> metadataTypes = node.getProvidedMetadataTypes();
		if( metadataTypes.size() > 0 ) {
			// Werte anzeigen
			for( String str : metadataTypes ) {
				if( node.getMetadataItem( str ) != null ) {
					TableItem item = new TableItem(table, 0);
					item.setText( 0, str );
					Object val = node.getMetadataItem( str ).getValue();
					item.setText( 1, val != null ? val.toString() : "null" );
				}
			}
		} 
		
		// Neu ausrichten
		valueCol.pack();
		nameCol.pack();
	}

	@Override
	public void nodeModelChanged( final INodeModel< IPhysicalOperator > sender ) {
		if( Display.getCurrent().isDisposed())
			return;

		Display.getCurrent().asyncExec( new Runnable() {
			@Override
			public void run() {
				if( !nameText.isDisposed() )
					nameText.setText( sender.getName() );
			}
		});
	}

	private Collection<String> getSelectedMetadataTypes() {
		TableItem[] items = table.getItems();
		Collection<String> strs = new ArrayList<String>();
		for( TableItem i : items )
			strs.add( i.getText(0) );
		return strs;
	}

	@Override
	public void metadataChanged( IMetadataModel< IMonitoringData< ? >> sender, String changedMetadataItemType ) {
		fillTable();
	}
	
}
