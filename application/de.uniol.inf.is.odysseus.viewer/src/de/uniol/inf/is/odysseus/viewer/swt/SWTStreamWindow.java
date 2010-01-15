package de.uniol.inf.is.odysseus.viewer.swt;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.viewer.model.graph.INodeModelChangeListener;
import de.uniol.inf.is.odysseus.viewer.model.stream.DefaultStreamConnection;
import de.uniol.inf.is.odysseus.viewer.model.stream.IStreamConnection;
import de.uniol.inf.is.odysseus.viewer.model.stream.IStreamElementListener;
import de.uniol.inf.is.odysseus.viewer.swt.diagram.SWTDiagramFactory;
import de.uniol.inf.is.odysseus.viewer.swt.resource.SWTResourceManager;
import de.uniol.inf.is.odysseus.viewer.view.graph.IOdysseusNodeView;
import de.uniol.inf.is.odysseus.viewer.view.stream.DiagramConverterPair;
import de.uniol.inf.is.odysseus.viewer.view.stream.DiagramInfo;
import de.uniol.inf.is.odysseus.viewer.view.stream.IDiagramFactory;
import de.uniol.inf.is.odysseus.viewer.view.stream.IStreamDiagram;
import de.uniol.inf.is.odysseus.viewer.view.stream.IStreamElementConverter;

public class SWTStreamWindow<In, Out> implements INodeModelChangeListener<IPhysicalOperator>, IStreamElementListener<In> {
	
//	private static final Logger logger = LoggerFactory.getLogger( SWTStreamWindow.class );

	private IStreamConnection<In> streamManager;
	private IStreamDiagram<Out> diagram;
	private IStreamElementConverter<In,Out> converter;
	private IOdysseusNodeView node;
	
	private final Composite composite;
	private final Shell parentShell;
	private final Label infoLabel;
	private int elementCounter;

	public SWTStreamWindow( final Shell parentShell, IOdysseusNodeView node, DiagramInfo diagramInfo ) {

		// Fenster designen
		this.parentShell = parentShell;
		parentShell.setText( node.getModelNode().getName() );
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		parentShell.setLayout( gridLayout );
		parentShell.addDisposeListener( new DisposeListener() {
			@Override
			public void widgetDisposed( DisposeEvent arg0 ) {
				close();
			}	
		});
			
		// Parameter übernehmen
		this.node = node;
		this.node.getModelNode().addNodeModelChangeListener( this );	
		
		ToolBar toolBar = new ToolBar(parentShell,  SWT.FLAT | SWT.WRAP | SWT.CENTER);
		final ToolItem playItem = new ToolItem( toolBar, SWT.PUSH);
		playItem.setImage( SWTResourceManager.getInstance().getImage( "play" ) );
		playItem.setToolTipText( "Beobachtung läuft" );
		playItem.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent e ) {
				if( streamManager.isConnected() ) {
					playItem.setImage( SWTResourceManager.getInstance().getImage( "pause" ) );
					playItem.setToolTipText( "Beobachtung gestoppt" );
					streamManager.disconnect();
				} else {
					playItem.setImage( SWTResourceManager.getInstance().getImage( "play" ) );
					playItem.setToolTipText( "Beobachtung läuft" );
					streamManager.connect();
				}
			}
		});
		
		final ToolItem freezeItem = new ToolItem(toolBar, SWT.PUSH);
		freezeItem.setImage( SWTResourceManager.getInstance().getImage( "unlock" )  );
		freezeItem.setToolTipText( "Aktualisierung läuft" );
		freezeItem.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent e ) {
				if( !streamManager.isEnabled() ) { 
					freezeItem.setImage( SWTResourceManager.getInstance().getImage( "lock" )  );
					freezeItem.setToolTipText( "Aktualisierung gesperrt" );
					streamManager.disable();
				} else {
					freezeItem.setImage( SWTResourceManager.getInstance().getImage( "unlock" )  );
					freezeItem.setToolTipText( "Aktualisierung läuft" );
					streamManager.enable();
				}
			}
		});
		
		ToolItem resetItem = new ToolItem(toolBar, SWT.PUSH);
		resetItem.setImage( SWTResourceManager.getInstance().getImage( "reset" )  );
		resetItem.setToolTipText( "Beobachtung zurücksetzen" );
		resetItem.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent e ) {
				MessageBox msgBox = new MessageBox(parentShell.getShell(), SWT.YES | SWT.NO | SWT.ICON_QUESTION);
				msgBox.setText( "Warnung" );
				msgBox.setMessage( "Soll die Aufzeichnung wirklich zurückgesetzt werden?" );
				if( msgBox.open() == SWT.YES )
					diagram.reset();
			}
		});
	
		ToolItem exitItem = new ToolItem( toolBar, SWT.PUSH);
		exitItem.setImage( SWTResourceManager.getInstance().getImage( "exit" ) );
		exitItem.setToolTipText( "Beobachtung beenden" );
		exitItem.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent e ) {
				parentShell.dispose();
			}
		});

		infoLabel = new Label( parentShell, SWT.SHADOW_IN );
		infoLabel.setText( "0 Datenelemente beobachtet      " );
		GridData infoData = new GridData();
		infoData.horizontalAlignment = GridData.END;
		infoLabel.setLayoutData( infoData );
				
		composite = new Composite(parentShell, 0);
		GridData listData = new GridData(GridData.FILL_BOTH);
		listData.horizontalSpan = 2;
		composite.setLayoutData( listData );
		
		// Diagramme und Converter erzeugen
		IDiagramFactory diagramFactory = new SWTDiagramFactory(composite);
		DiagramConverterPair<In,Out> pair = diagramFactory.create( node.getModelNode(), diagramInfo );
		this.converter = pair.getConverter();
		this.diagram = pair.getDiagram();
		
		connectToStream();
	}
	
	@SuppressWarnings("unchecked")
	private void connectToStream() {
		// Datenstromquellen identifizieren
		final Collection<ISource<? extends In>> sources = new ArrayList<ISource<? extends In>>();
		IPhysicalOperator content = node.getModelNode().getContent();
		if( content instanceof ISource ) {
			sources.add( (ISource<? extends In>)content );
		} else if( content instanceof ISink ) {
			Collection list = ((ISink<?>)content).getSubscribedToSource();
			for( Object obj : list ) 
				sources.add( (ISource<? extends In>)((PhysicalSubscription<?>)obj).getTarget() );
		} else 
			throw new IllegalArgumentException("could not identify type of content of node " + node );

		// Datenstromverbindung aufbauen
		streamManager = new DefaultStreamConnection<In>(sources);
		streamManager.addStreamElementListener( this );
		streamManager.connect();
		
	}
	
	private void close() {
		streamManager.disconnect();
		node.getModelNode().removeNodeModelChangeListener( this );
	}

	public Composite getComposite() {
		return composite;
	}

	@Override
	public void nodeModelChanged( INodeModel< IPhysicalOperator > sender ) {
		// Titel anpassen, wenn sich der Name ändert
		if( !sender.getName().equals(parentShell.getText()))
			parentShell.setText( sender.getName() );
	}

	@Override
	public void streamElementRecieved( final In element, final int port ) {
		if( parentShell.isDisposed() || Display.getDefault() == null || Display.getDefault().isDisposed() )
			return;
		
		Display.getDefault().asyncExec( new Runnable() {
			@Override
			public void run() {
				try {
					if( infoLabel.isDisposed() )
						return;
					
					// Umwandeln und ans Diagramm schicken
					diagram.dataElementRecived( converter.convertStreamElement( element ), port );
					
					elementCounter++;
					infoLabel.setText( elementCounter + " Datenelemente beobachtet        ");
				} catch( Exception ex ) {
					ex.printStackTrace();
				}
			}		
		});
		
	}
}
