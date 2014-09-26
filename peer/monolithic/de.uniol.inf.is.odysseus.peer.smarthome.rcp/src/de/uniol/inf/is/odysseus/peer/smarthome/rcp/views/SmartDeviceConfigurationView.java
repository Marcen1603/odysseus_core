package de.uniol.inf.is.odysseus.peer.smarthome.rcp.views;

import net.jxta.peer.PeerID;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.rcp.SmartHomeRCPActivator;

public class SmartDeviceConfigurationView extends ViewPart implements IPeerCommunicatorListener {
	private static final Logger LOG = LoggerFactory.getLogger(SmartDeviceConfigurationView.class);
	private PeerID selectedPeer;

	public SmartDeviceConfigurationView() {
	}

	// the listener we register with the selection service 
		private ISelectionListener listener = new ISelectionListener() {
			public void selectionChanged(IWorkbenchPart sourcepart, ISelection selection) {
				
				if(sourcepart instanceof SmartDeviceView){
					//System.out.println("SmartDeviceView!!!!!");
					
					if (selection instanceof IStructuredSelection) {
						IStructuredSelection ss = (IStructuredSelection) selection;
						
						if(ss.size()==1){
							Object firstElement = ss.getFirstElement();
							
							if (firstElement instanceof PeerID) {
								PeerID selectedPeer = (PeerID)firstElement;
								
								SmartDeviceConfigurationView.this.selectedPeer = selectedPeer;
								
								SmartDeviceConfigurationView.this.peerIDText.setText(selectedPeer.intern().toString());
								SmartDeviceConfigurationView.this.peerIDText.setEditable(false);
								
								//Open Configuration for PeerID:selectedPeer.intern()
								//1. get the current configuration from the peer
								//2. display the configuration
								//3. click on save button: send the new configuration to the peer
								
								
								
								
							}
						}else{
							SmartDeviceConfigurationView.this.peerIDText.setText("");
						}
					}
				}
				
				//System.out.println("SmartDeviceConfigurationView selectionChanged:");
				
				/*
				if (selection instanceof IStructuredSelection) {
					IStructuredSelection ss = (IStructuredSelection) selection;
					showItems(ss.toArray());
				}
				if (selection instanceof ITextSelection) {
					ITextSelection ts  = (ITextSelection) selection;
					showText(ts.getText());
				}
				if (selection instanceof IMarkSelection) {
					IMarkSelection ms = (IMarkSelection) selection;
					try {
					    showText(ms.getDocument().get(ms.getOffset(), ms.getLength()));
					} catch (BadLocationException ble) { }
				}
				*/
			}
		};
		private Text peerIDText;
		private Text peerContextNameText;
	
	@Override
	public void createPartControl(Composite parent) {
		//@SuppressWarnings("unused")
		//final Composite tableComposite = new Composite(parent, SWT.NONE);
		
		
		parent.setLayout(new FormLayout());
		
		Label lblPeerid = new Label(parent, SWT.NONE);
		FormData fd_lblPeerid = new FormData();
		fd_lblPeerid.top = new FormAttachment(0, 10);
		fd_lblPeerid.left = new FormAttachment(0, 10);
		lblPeerid.setLayoutData(fd_lblPeerid);
		lblPeerid.setText("PeerID");
		
		Label lblContextName = new Label(parent, SWT.NONE);
		FormData fd_lblContextName = new FormData();
		fd_lblContextName.top = new FormAttachment(lblPeerid, 14);
		fd_lblContextName.left = new FormAttachment(0, 10);
		lblContextName.setLayoutData(fd_lblContextName);
		lblContextName.setText("Context name");
		
		peerIDText = new Text(parent, SWT.BORDER);
		FormData fd_peerIDText = new FormData();
		fd_peerIDText.right = new FormAttachment(100, -80);
		fd_peerIDText.left = new FormAttachment(lblPeerid, 115);
		peerIDText.setLayoutData(fd_peerIDText);
		
		peerContextNameText = new Text(parent, SWT.BORDER);
		fd_peerIDText.bottom = new FormAttachment(peerContextNameText, -6);
		FormData fd_peerContextNameText = new FormData();
		fd_peerContextNameText.right = new FormAttachment(peerIDText, 0, SWT.RIGHT);
		fd_peerContextNameText.left = new FormAttachment(lblContextName, 74);
		fd_peerContextNameText.top = new FormAttachment(lblContextName, -3, SWT.TOP);
		peerContextNameText.setLayoutData(fd_peerContextNameText);
		
		Button btnSave = new Button(parent, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Save configuration");
				
				if(selectedPeer!=null){
					try {
						SmartDeviceMessage chat = new SmartDeviceMessage("Save configuration");
						SmartHomeRCPActivator.getPeerCommunicator().send(selectedPeer, chat);
					} catch (PeerCommunicationException ex) {
						LOG.error("Cannot send message", ex);
					}
				}
			}
		});
		FormData fd_btnSave = new FormData();
		btnSave.setLayoutData(fd_btnSave);
		btnSave.setText("Save configuration");
		
		Button btnLoadConfiguration = new Button(parent, SWT.NONE);
		fd_btnSave.left = new FormAttachment(btnLoadConfiguration, 21);
		btnLoadConfiguration.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Load configuration");
				if(selectedPeer!=null){
					try {
						SmartDeviceMessage chat = new SmartDeviceMessage("Load configuration");
						SmartHomeRCPActivator.getPeerCommunicator().send(selectedPeer, chat);
					} catch (PeerCommunicationException ex) {
						LOG.error("Cannot send message", ex);
					}
				}
			}
		});
		fd_btnSave.top = new FormAttachment(btnLoadConfiguration, 0, SWT.TOP);
		FormData fd_btnLoadConfiguration = new FormData();
		fd_btnLoadConfiguration.top = new FormAttachment(lblContextName, 68);
		fd_btnLoadConfiguration.left = new FormAttachment(lblPeerid, 0, SWT.LEFT);
		btnLoadConfiguration.setLayoutData(fd_btnLoadConfiguration);
		btnLoadConfiguration.setText("Load configuration");
		
		
		setPartName("Smart Device Configuration");
		
		
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(listener);
		
		
		//RCPP2PNewPlugIn.getPeerCommunicator().registerMessageType(SmartDeviceMessage.class);
		//RCPP2PNewPlugIn.getPeerCommunicator().addListener(this, SmartDeviceMessage.class);
		try{
			SmartHomeRCPActivator.getPeerCommunicator().registerMessageType(SmartDeviceMessage.class);
			SmartHomeRCPActivator.getPeerCommunicator().addListener(this, SmartDeviceMessage.class);
		}catch(NullPointerException ex){
			ex.printStackTrace();
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		System.out.println("SmartDeviceConfiguration receivedMessage:");
		System.out.println("senderPeer:"+senderPeer.intern());
		//System.out.println("message:"+message.toString());
		//System.out.println("message:"+message.fromBytes());
		
		if(message instanceof SmartDeviceMessage){
			SmartDeviceMessage sdmsg = (SmartDeviceMessage)message;
			System.out.println("message: "+sdmsg.getText());
		}else{
			
		}
	}
}
