package de.uniol.inf.is.odysseus.rcp.p2p_new.login;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.P2PNetworkException;
import de.uniol.inf.is.odysseus.rcp.login.ILoginContribution;
import de.uniol.inf.is.odysseus.rcp.login.ILoginContributionContainer;
import de.uniol.inf.is.odysseus.rcp.p2p_new.RCPP2PNewPlugIn;

public class P2PLoginContribution implements ILoginContribution {

	private static final Logger LOG = LoggerFactory.getLogger(P2PLoginContribution.class);
	
	private static final String PEER_NAME_SYS_PROPERTY = "peer.name";
	private static final String PEER_GROUP_NAME_SYS_PROPERTY = "peer.group.name";

	private String groupName;
	private String peerName;
	
	@Override
	public void onInit() {
	}

	@Override
	public void onLoad(Map<String, String> savedConfig) {
		Optional<String> optPeerName = determinePeerName();
		Optional<String> optGroupName = determinePeerGroupName();
		
		if( optPeerName.isPresent() ) {
			peerName = optPeerName.get();
		} else {
			peerName = savedConfig.get("peer.name");
			if( peerName == null ) {
				peerName = "";
			}
		}
		
		if( optGroupName.isPresent() ) {
			groupName = optGroupName.get();
		} else {
			groupName = savedConfig.get("peer.groupname");
			
			if( groupName == null ) {
				groupName = "";
			}
		}
	}

	@Override
	public boolean isValid() {
		return !Strings.isNullOrEmpty(peerName) && !Strings.isNullOrEmpty(groupName);
	}

	@Override
	public String getTitle() {
		return "P2P";
	}

	@Override
	public void createPartControl(Composite parent, final ILoginContributionContainer container) {
		Composite rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayout(new GridLayout(2, false));
		rootComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		createLabel(rootComposite, "Peer name");
		final Text peerNameText = new Text(rootComposite, SWT.BORDER);
		peerNameText.setText(peerName);
		peerNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		peerNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				peerName = peerNameText.getText();
				
				container.changed();
				updateMessages(container);
			}

		});
		
		createLabel(rootComposite, "Group name");
		final Text groupNameText = new Text(rootComposite, SWT.BORDER);
		groupNameText.setText(groupName);
		groupNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		groupNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				groupName = groupNameText.getText();
				
				container.changed();
			}
		});
	}
	
	private void updateMessages(ILoginContributionContainer container) {
		if( Strings.isNullOrEmpty(peerName)) {
			container.setErrorMessage("Peer name must be specified");
		} else if( Strings.isNullOrEmpty(groupName)) {
			container.setErrorMessage("Peer group name must be specified");
		} else {
			container.setErrorMessage(null);
		}
	}

	private static void createLabel(Composite rootComposite, String text) {
		Label peerNameLabel = new Label( rootComposite, SWT.NONE);
		peerNameLabel.setText(text);
	}

	@Override
	public void dispose() {
	}

	@Override
	public Map<String, String> onSave() {
		Map<String, String> saved = Maps.newHashMap();
		saved.put("peer.name", peerName);
		saved.put("peer.groupname", groupName);
		return saved;
	}

	@Override
	public boolean onFinish() {
		waitForP2PNetworkManager();
		
		IP2PNetworkManager networkManager = RCPP2PNewPlugIn.getP2PNetworkManager();
		String currentPeerName = networkManager.getLocalPeerName();
		String currentGroupName = networkManager.getLocalPeerGroupName();
		
		if( !peerName.equals(currentPeerName) || !groupName.equals(currentGroupName)) {
			if( networkManager.isStarted() ) {
				networkManager.stop();
			}
			
			networkManager.setLocalPeerGroupName(groupName);
			networkManager.setLocalPeerName(peerName);
			
			return tryStartNetwork(networkManager);
		}
		return true;
	}

	private static void waitForP2PNetworkManager() {
		while( RCPP2PNewPlugIn.getP2PNetworkManager() == null ) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
			}
		}
	}

	private static boolean tryStartNetwork(IP2PNetworkManager networkManager) {
		try {
			networkManager.start();
			return true;
		} catch (P2PNetworkException ex) {
			LOG.error("Could not start p2p network", ex);
			return false;
		}
	}

	private static Optional<String> determinePeerName() {
		String peerName = System.getProperty(PEER_NAME_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(peerName)) {
			return Optional.of(peerName);
		}

		peerName = OdysseusConfiguration.get(PEER_NAME_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(peerName)) {
			return Optional.of(peerName);
		}

		return Optional.absent();
	}
	
	private static Optional<String> determinePeerGroupName() {
		String peerGroupName = System.getProperty(PEER_GROUP_NAME_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(peerGroupName)) {
			return Optional.of(peerGroupName);
		}

		peerGroupName = OdysseusConfiguration.get(PEER_GROUP_NAME_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(peerGroupName)) {
			return Optional.of(peerGroupName);
		}

		return Optional.absent();
	}
	
	@Override
	public int getPriority() {
		return 0;
	}
}
