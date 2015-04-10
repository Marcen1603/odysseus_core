package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.views;

import net.jxta.peer.PeerID;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.peer.communication.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.SmartHomeRCPActivator;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDeviceLocalConfigurationServer;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDevicePublisher;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.config.SmartDeviceConfig;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.config.SmartDeviceConfigurationRequestMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.config.SmartDeviceConfigurationResponseMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.message.SmartDeviceMessage;

public class SmartDeviceConfigurationEditViewPart extends ViewPart implements IPeerCommunicatorListener {
	private static final Logger LOG = LoggerFactory.getLogger(SmartDeviceConfigurationEditViewPart.class);
	public static final String ID = "de.uniol.inf.is.odysseus.rcp.peer.SmartDeviceConfigurationView";
	private PeerID selectedPeer;

	public SmartDeviceConfigurationEditViewPart() {
	}
	
	private ISelectionListener listener = new ISelectionListener() {
		@Override
		public void selectionChanged(IWorkbenchPart sourcepart, ISelection selection) {
			
			if(sourcepart instanceof SmartDeviceShowView){
				//System.out.println("SmartDeviceView!!!!!");
				
				if (selection instanceof IStructuredSelection) {
					IStructuredSelection ss = (IStructuredSelection) selection;
					
					if(ss.size()==1){
						Object firstElement = ss.getFirstElement();
						
						if (firstElement instanceof PeerID) {
							PeerID selectedPeer = (PeerID)firstElement;
							
							SmartDeviceConfigurationEditViewPart.this.selectedPeer = selectedPeer;
							
							SmartDeviceConfigurationEditViewPart.this.peerIDText.setText(selectedPeer.intern().toString());
							SmartDeviceConfigurationEditViewPart.this.peerIDText.setEditable(false);
							
							loadConfiguration();
						}
					}else{
						clearSelection();
					}
				}
			}
		}
	};
	private Text peerIDText;
	private Text peerContextNameText;
	
	private void clearSelection() {
		SmartDeviceConfigurationEditViewPart.this.selectedPeer = null;
		SmartDeviceConfigurationEditViewPart.this.peerIDText.setText("");
		SmartDeviceConfigurationEditViewPart.this.peerContextNameText.setText("");
	}
	
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
				saveConfiguration();
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
				if(selectedPeer!=null){
					loadConfiguration();
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
		
		
		try{
			SmartHomeRCPActivator.getPeerCommunicator().registerMessageType(SmartDeviceConfigurationResponseMessage.class);
			SmartHomeRCPActivator.getPeerCommunicator().registerMessageType(SmartDeviceConfigurationRequestMessage.class);
			
			SmartHomeRCPActivator.getPeerCommunicator().addListener(this, SmartDeviceConfigurationResponseMessage.class);
		}catch(NullPointerException ex){
			ex.printStackTrace();
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	private void loadConfiguration() {
		try {
			SmartDeviceConfigurationRequestMessage confRequest = new SmartDeviceConfigurationRequestMessage("Configuration");
			SmartHomeRCPActivator.getPeerCommunicator().send(selectedPeer, confRequest);
			
		} catch (PeerCommunicationException ex) {
			LOG.error("Cannot load configuration", ex);
		}
	}
	
	private void saveConfiguration() {
		if(selectedPeer!=null){
			try {
				SmartDeviceConfig config = new SmartDeviceConfig();
				config.setContextname(SmartDeviceConfigurationEditViewPart.this.peerContextNameText.getText());
				
				if(SmartDevicePublisher.getInstance().isLocalPeer(selectedPeer)){
					SmartDeviceLocalConfigurationServer.getInstance().overWriteSmartDeviceConfigWith(config);
					SmartDeviceLocalConfigurationServer.getInstance().saveSmartDeviceConfig();
					
				}else{
					SmartDeviceConfigurationResponseMessage smartDeviceConfigResponse = new SmartDeviceConfigurationResponseMessage();
					smartDeviceConfigResponse.setSmartDeviceConfig(config);
					
					SmartHomeRCPActivator.getPeerCommunicator().send(selectedPeer, smartDeviceConfigResponse);
				}
			} catch (PeerCommunicationException ex) {
				LOG.error("Cannot send message", ex);
			}
		}
	}
	
	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		if(message instanceof SmartDeviceMessage){
			// SmartDeviceMessage sdmsg = (SmartDeviceMessage)message;
			
			
			
		}else if(message instanceof SmartDeviceConfigurationResponseMessage){
			SmartDeviceConfigurationResponseMessage responseMessage = (SmartDeviceConfigurationResponseMessage)message;
			SmartDeviceConfig responseDeviceConfig = responseMessage.getSmartDeviceConfig();
			
			setPeerContextName(senderPeer, responseDeviceConfig.getContextname());
		}
	}
	
	public void setPeerContextName(final PeerID senderPeer, final String contextName){
		Display.getDefault().syncExec(new Runnable() {
		    @Override
			public void run() {
		    	if(isSelectedPeer(senderPeer)){
		    		SmartDeviceConfigurationEditViewPart.this.peerContextNameText.setText(contextName);
		    	}
		    }
		});
	}
	
	public boolean isSelectedPeer(PeerID peer)
	{
		if(SmartDeviceConfigurationEditViewPart.this.selectedPeer==null || SmartDeviceConfigurationEditViewPart.this.selectedPeer.intern()==null){
			return false;
		}else if(peer==null || peer.intern()==null){
			return false;
		}
		
		String str1 = SmartDeviceConfigurationEditViewPart.this.selectedPeer.intern().toString();
		String str2 = peer.intern().toString();
		
		if(str1==null || str2==null) return false;
		return str1.equals(str2);
	}
}
