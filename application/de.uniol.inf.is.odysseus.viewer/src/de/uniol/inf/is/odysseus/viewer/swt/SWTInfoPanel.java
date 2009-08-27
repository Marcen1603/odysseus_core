package de.uniol.inf.is.odysseus.viewer.swt;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
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
import de.uniol.inf.is.odysseus.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.viewer.model.graph.INodeModelChangeListener;
import de.uniol.inf.is.odysseus.viewer.model.graph.IOdysseusNodeModel;
import de.uniol.inf.is.odysseus.viewer.model.graph.OdysseusNodeModel;
import de.uniol.inf.is.odysseus.viewer.model.meta.IMetadataChangeListener;
import de.uniol.inf.is.odysseus.viewer.model.meta.IMetadataModel;
import de.uniol.inf.is.odysseus.viewer.model.meta.OdysseusMetadataProvider;
import de.uniol.inf.is.odysseus.viewer.swt.resource.SWTResourceManager;
import de.uniol.inf.is.odysseus.viewer.swt.symbol.AbstractSWTSymbolElement;
import de.uniol.inf.is.odysseus.viewer.view.graph.IOdysseusNodeView;
import de.uniol.inf.is.odysseus.viewer.view.graph.Vector;
import de.uniol.inf.is.odysseus.viewer.view.symbol.ISymbolElement;
import de.uniol.inf.is.odysseus.viewer.view.symbol.SymbolElementContainer;

public class SWTInfoPanel implements INodeModelChangeListener<IPhysicalOperator>, IMetadataChangeListener<IMonitoringData<?>> {

	public interface ISWTInfoPanelListener {
		public void showDataStream( SWTInfoPanel sender );
		public void pinChanged( SWTInfoPanel sender );
		public void selectNode( SWTInfoPanel sender );
	}
	
	private static final Logger logger = LoggerFactory.getLogger( SWTInfoPanel.class );
	
	private final Composite composite;
	
	private final Table table;
	private final TableColumn valueCol;
	private final TableColumn nameCol;
	
	private final IOdysseusNodeModel node;
	private final IOdysseusNodeView nodeView;
	private final Canvas symbolCanvas;	
	private final Text nameText;
	private final Label endGradeLabel;
	private final Label startGradeLabel;
	
	private final Button checkDataButton;
	
	private final Collection<ISWTInfoPanelListener> listeners = new ArrayList<ISWTInfoPanelListener>();
	
	private boolean pinned;
	
	// Workaround für das Ausblenden
	private int oldWidth;
	private int oldHeight;
	
	public SWTInfoPanel( final Composite parent, final IOdysseusNodeView nodeToShow ) {
				
		if( parent == null ) 
			throw new IllegalArgumentException("parent is null!");
		
		if( nodeToShow == null ) 
			throw new IllegalArgumentException("nodeToShow is null!");
		
		node = nodeToShow.getModelNode();
		if( node == null ) 
			throw new IllegalArgumentException("nodeToShow has no modelnode!");
				
		// Parameter übernehmen
		nodeView = nodeToShow;
		
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
		
		// Namenszeile
		symbolCanvas = new Canvas(composite, SWT.BORDER);
		symbolCanvas.setSize( nodeToShow.getWidth(), nodeToShow.getHeight() );
		symbolCanvas.addPaintListener( new PaintListener() {
			@Override
			public void paintControl( PaintEvent e ) {
				GC gc = e.gc;
				SymbolElementContainer<IPhysicalOperator> symbol = nodeView.getSymbolContainer();
				for( ISymbolElement<IPhysicalOperator> ele : symbol ) {
					if( ele instanceof AbstractSWTSymbolElement<?>) {
						((AbstractSWTSymbolElement<IPhysicalOperator>)ele).setActualGC( gc );
						ele.draw( Vector.EMPTY_VECTOR, nodeView.getWidth(), nodeView.getHeight(), 1.0f );
					}
				}
			}
		});
		data = new GridData();
		data.heightHint = nodeView.getHeight() + 1;
		data.widthHint = nodeView.getWidth() + 1;
		symbolCanvas.setLayoutData( data );
		
		nameText = new Text( composite, SWT.SINGLE );
		nameText.setText( node.getName());
		nameText.setLayoutData( new GridData(GridData.FILL_HORIZONTAL) );
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
		data = new GridData();
		data.horizontalAlignment = GridData.END;
		buttonList.setLayoutData( data );
		gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		buttonList.setLayout( gridLayout );
		
		// Sichtbarkeitsbutton
		final Button visibleButton = new Button( buttonList, SWT.TOGGLE );
		visibleButton.setImage( SWTResourceManager.getInstance().getImage( "hide" ) );
		visibleButton.setToolTipText( "Verkleinern" );
		
		// Pinbutton
		final Button pinButton = new Button( buttonList, SWT.TOGGLE );
		pinButton.setImage( SWTResourceManager.getInstance().getImage( "pin" ) );
		pinButton.setToolTipText( "Flüchtig" );
		pinButton.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent e ) {
				pinned = pinButton.getSelection();
				if( pinned ) {
					pinButton.setToolTipText( "Fixiert" );
					pinButton.setImage( SWTResourceManager.getInstance().getImage( "pinned" ) );
				} else {
					pinButton.setToolTipText( "Flüchtig" );
					pinButton.setImage( SWTResourceManager.getInstance().getImage( "pin" ) );
				}
				notifyPinChanged();
			}
		});
		
		final Button selectButton = new Button( buttonList, SWT.PUSH );
		selectButton.setImage( SWTResourceManager.getInstance().getImage( "select" ));
		selectButton.setToolTipText( "Knoten auswählen" );
		selectButton.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent arg0 ) {
				notifySelectNode();
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
					visibleButton.setImage(SWTResourceManager.getInstance().getImage( "hide" ) );
					visibleButton.setToolTipText( "Verkleinern" );
					parent.layout();
				} else {
					content.setVisible( false );
					oldWidth = content.getSize().x;
					oldHeight = content.getSize().y;
					contentGridData.widthHint = 0;
					contentGridData.heightHint = 0;
					visibleButton.setImage(SWTResourceManager.getInstance().getImage( "show" ) );
					visibleButton.setToolTipText( "Vergrößern" );
					parent.layout();
				}
			}
		});
		
		// Operatortyp-anzeige
		if( nodeToShow.getModelNode().getContent() != null ) {
			Label operatorName = new Label(content, SWT.SHADOW_IN);
			operatorName.setText( "Operatortyp: " + nodeToShow.getModelNode().getContent().getClass().getSimpleName() );
			data = new GridData( GridData.FILL_HORIZONTAL );
			data.horizontalSpan = 2;
			operatorName.setLayoutData( data );
		}
				
		// Nachbarschaftszeile
		endGradeLabel = new Label(content, SWT.SHADOW_IN);
		endGradeLabel.setText( "#Parents:" + node.getConnectionsAsEndNode().size()  );
		data = new GridData( GridData.FILL_HORIZONTAL );
		data.horizontalSpan = 2;
		endGradeLabel.setLayoutData( data );
		
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
		nameCol.setText( "Metadatentyp" );
		
		valueCol = new TableColumn(table, SWT.NONE);
		valueCol.setText( "Wert" );
		fillTable();
		
		Composite tableEditContent = new Composite( content, 0 );
		GridLayout gl = new GridLayout();
		gl.numColumns = 4;
		tableEditContent.setLayout( gl  );
		GridData tabelEditGridData = new GridData();
		tabelEditGridData.horizontalSpan = 2;
		tableEditContent.setLayoutData( tabelEditGridData );
		
		Button addMetadataButton = new Button( tableEditContent, SWT.PUSH );
		addMetadataButton.setImage( SWTResourceManager.getInstance().getImage( "metaNew" ) );
		addMetadataButton.setToolTipText( "Metadatum hinzufügen" );
		addMetadataButton.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent e ) {
				// Dialog öffnen
				new SWTMetadataSelectWindow(parent.getShell(), new OdysseusMetadataProvider(), (OdysseusNodeModel)node );
			}
		});
		
		Button removeMetadataButton = new Button( tableEditContent, SWT.PUSH );
		removeMetadataButton.setImage( SWTResourceManager.getInstance().getImage( "metaRemove" ) );
		removeMetadataButton.setToolTipText( "Metadatum entfernen" );
		removeMetadataButton.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent e ) {
				TableItem[] selectedItems = table.getSelection();
				if( selectedItems.length > 0 ) {
					MessageBox msgBox = new MessageBox( parent.getShell(), SWT.YES | SWT.NO | SWT.ICON_QUESTION );
					msgBox.setText( "Warnung" );
					msgBox.setMessage( selectedItems.length + " Metadaten wirklich entfernen?" );
					if( msgBox.open() == SWT.YES ) {
						for( TableItem item : selectedItems ) {
							if( nodeToShow.getModelNode() != null ) {
								nodeToShow.getModelNode().removeMetadataItem( item.getText(0) );
							}
						}
					}
				}
			}
		});
		
		Button resetMetadataButton = new Button( tableEditContent, SWT.PUSH );
		resetMetadataButton.setImage( SWTResourceManager.getInstance().getImage( "metaReset" ) );
		resetMetadataButton.setToolTipText( "Metadatum resetten" );
		resetMetadataButton.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent e ) {
				Collection<String> selectedTypes = getSelectedMetadataTypes();
				for( String metaType : selectedTypes ) {
					if( nodeToShow.getModelNode() != null ) {
						nodeToShow.getModelNode().resetMetadataItem( metaType );
					}
				}
			}
		});
		
		Button refreshMetadataButton = new Button( tableEditContent, SWT.PUSH );
		refreshMetadataButton.setImage( SWTResourceManager.getInstance().getImage( "metaReload" ) );
		refreshMetadataButton.setToolTipText( "Metadatum aktualisieren" );
		refreshMetadataButton.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent arg0 ) {
				fillTable();
			}
		});
		
		// Button zum Beobachten von Daten
		checkDataButton = new Button(content, SWT.PUSH);
		checkDataButton.setText( "Datenstrom beobachten" );
		GridData checkDataGridData = new GridData(GridData.FILL_HORIZONTAL);
		checkDataGridData.horizontalSpan = 2;
		checkDataButton.setLayoutData( checkDataGridData );
		checkDataButton.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent e ) {
				notifyShowDataStream();
			}
		});
				
		logger.debug( "NodeInfoPanel for node '" + node + "' created." );
		
		node.addNodeModelChangeListener( this );
		node.addMetadataChangeListener( this );
	}
	
	public final IOdysseusNodeView getNodeDisplay() {
		return nodeView;
	}
		
	public final Composite getComposite() {
		return composite;
	}
	
	public void addInfoPanelListener( ISWTInfoPanelListener listener ) {
		if( !listeners.contains( listener ) ) {
			synchronized(listeners) {
				listeners.add( listener );
			}
		}
	}
	
	public void removeInfoPanelListener( ISWTInfoPanelListener listener ) {
		if( listeners.contains( listener )) {
			synchronized( listeners ) {
				listeners.remove( listener );
			}
		}
	}
	
	public void notifyShowDataStream() {
		if( listeners.isEmpty())
			return;
		
		synchronized(listeners) {
			for( ISWTInfoPanelListener listener : listeners.toArray( new ISWTInfoPanelListener[0] ) ) {
				if( listener != null ) {
					listener.showDataStream( this );
				}
			}
		}
	}
	
	public void notifyPinChanged() {
		if( listeners.isEmpty())
			return;
		
		synchronized(listeners) {
			for( ISWTInfoPanelListener listener : listeners.toArray( new ISWTInfoPanelListener[0] ) ) {
				if( listener != null ) {
					listener.pinChanged( this );
				}
			}
		}
	}
	
	public void notifySelectNode() {
		if( listeners.isEmpty())
			return;
		
		synchronized(listeners) {
			for( ISWTInfoPanelListener listener : listeners.toArray( new ISWTInfoPanelListener[0] ) ) {
				if( listener != null ) {
					listener.selectNode( this );
				}
			}
		}
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
		
		if( symbolCanvas.isDisposed() )
			return;
			
		Display.getCurrent().asyncExec( new Runnable() {
			@Override
			public void run() {
				if( !symbolCanvas.isDisposed() )
					symbolCanvas.redraw();
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
