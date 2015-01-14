package de.uniol.inf.is.odysseus.peer.rcp.login;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.P2PNetworkException;
import de.uniol.inf.is.odysseus.p2p_new.util.InetAddressUtil;
import de.uniol.inf.is.odysseus.peer.config.PeerConfiguration;
import de.uniol.inf.is.odysseus.peer.rcp.RCPP2PNewPlugIn;
import de.uniol.inf.is.odysseus.rcp.login.ILoginContribution;
import de.uniol.inf.is.odysseus.rcp.login.ILoginContributionContainer;

public class P2PLoginContribution implements ILoginContribution {

	private static final String ADDRESS_PREFIX = "tcp://";

	private static final Logger LOG = LoggerFactory.getLogger(P2PLoginContribution.class);

	private static final String PEER_NAME_SYS_PROPERTY = "peer.name";
	private static final String PEER_GROUP_NAME_SYS_PROPERTY = "peer.group.name";
	private static final String RENDEVOUS_ADDRESS_SYS_PROPERTY = "peer.rdv.address";
	private static final String RENDEVOUS_ACTIVE_SYS_PROPERTY = "peer.rdv.active";
	private static final String PEER_PORT_SYS_PROPERTY = "peer.port";

	private String groupName;
	private String peerName;
	private int peerPort;
	private String rendevousAddress;
	private boolean rendevousActive;

	@Override
	public void onInit() {
	}

	@Override
	public void onLoad(Map<String, String> savedConfig) {
		Optional<String> optPeerName = determinePeerName();
		Optional<String> optGroupName = determinePeerGroupName();
		Optional<String> optRendevousAddress = determineRendevousAddress();
		Optional<Boolean> optRendevousActive = determineRendevousActive();
		Optional<Integer> optPeerPort = determinePeerPort();

		if (optPeerName.isPresent()) {
			peerName = optPeerName.get();
		} else {
			peerName = savedConfig.get(PEER_NAME_SYS_PROPERTY);
			if (peerName == null) {
				try {
					peerName = InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException e) {
					peerName = "";
				}
			}
		}

		if (optGroupName.isPresent()) {
			groupName = optGroupName.get();
		} else {
			groupName = savedConfig.get(PEER_GROUP_NAME_SYS_PROPERTY);

			if (groupName == null) {
				groupName = "";
			}
		}

		if (optRendevousAddress.isPresent()) {
			rendevousAddress = optRendevousAddress.get();
			if( rendevousAddress.equalsIgnoreCase("none")) {
				rendevousAddress = "";
			}
		} else {
			rendevousAddress = savedConfig.get(RENDEVOUS_ADDRESS_SYS_PROPERTY);

			if (rendevousAddress == null) {
				rendevousAddress = "";
			}
			
			if( rendevousAddress.equalsIgnoreCase("none")) {
				rendevousAddress = "";
			}
		}

		if (optRendevousActive.isPresent()) {
			rendevousActive = optRendevousActive.get();
		} else {
			try {
				String rendevousActiveStr = savedConfig.get(RENDEVOUS_ACTIVE_SYS_PROPERTY);

				if (rendevousActiveStr == null) {
					rendevousActive = false;
				}

				rendevousActive = Boolean.valueOf(rendevousActiveStr);
			} catch (Throwable t) {
				LOG.error("Could not get if rendevous peer is active here", t);
				rendevousActive = false;
			}
		}
		
		if (optPeerPort.isPresent()) {
			peerPort = optPeerPort.get();
		} else {
			try {
				String peerPortStr = savedConfig.get(PEER_PORT_SYS_PROPERTY);
				
				if( peerPortStr == null ) {
					peerPort = determineRandomPort();
				}
				
				peerPort = Integer.valueOf(peerPortStr);
			} catch( Throwable t ) {
				LOG.error("Could not determine port", t);
				peerPort = determineRandomPort();
			}
		}

	}

	private static int determineRandomPort() {
		return new Random().nextInt(20000) + 10000;
	}

	private static Optional<String> determinePeerName() {
		String peerName = System.getProperty(PEER_NAME_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(peerName)) {
			return Optional.of(InetAddressUtil.replaceWithIPAddressIfNeeded(peerName));
		}

		Optional<String> optName = PeerConfiguration.get(PEER_NAME_SYS_PROPERTY);
		
		if (optName.isPresent()) {
			String peer = optName.get();
			return Optional.of(InetAddressUtil.replaceWithIPAddressIfNeeded(peer));
		}

		return Optional.absent();
	}

	private static Optional<String> determinePeerGroupName() {
		String peerGroupName = System.getProperty(PEER_GROUP_NAME_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(peerGroupName)) {
			return Optional.of(peerGroupName);
		}

		return PeerConfiguration.get(PEER_GROUP_NAME_SYS_PROPERTY);
	}

	private static Optional<String> determineRendevousAddress() {
		String rendevousAddress = System.getProperty(RENDEVOUS_ADDRESS_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(rendevousAddress)) {
			return Optional.of(rendevousAddress);
		}

		return PeerConfiguration.get(RENDEVOUS_ADDRESS_SYS_PROPERTY);
	}

	private static Optional<Boolean> determineRendevousActive() {
		try {
			String rendevousAddress = System.getProperty(RENDEVOUS_ACTIVE_SYS_PROPERTY);
			if (!Strings.isNullOrEmpty(rendevousAddress)) {
				return Optional.of(Boolean.valueOf(rendevousAddress));
			}

			Optional<String> optAddress = PeerConfiguration.get(RENDEVOUS_ACTIVE_SYS_PROPERTY);
			if (optAddress.isPresent()) {
				return Optional.of(Boolean.valueOf(optAddress.get()));
			}
		} catch (Throwable t) {
			LOG.error("Could not determine if this peer is a rendevous peer", t);
		}
		return Optional.absent();
	}

	private static Optional<Integer> determinePeerPort() {
		try {
			String portString = System.getProperty(PEER_PORT_SYS_PROPERTY);
			if (!Strings.isNullOrEmpty(portString)) {
				return Optional.of(Integer.valueOf(portString));
			}
	
			Optional<String> optPort = PeerConfiguration.get(PEER_PORT_SYS_PROPERTY);
			if (optPort.isPresent()) {
				return Optional.of(Integer.valueOf(optPort.get()));
			}
		} catch( Throwable t ) {
			LOG.error("Could not determine port", t);
		}

		return Optional.absent();
	}

	@Override
	public boolean isValid() {
		return !Strings.isNullOrEmpty(peerName) && !Strings.isNullOrEmpty(groupName) && ( peerPort > 1023 && peerPort <= 65535 ) && tryToURIIfNotNull(rendevousAddress);
	}

	@Override
	public String getTitle() {
		return "P2P";
	}

	@Override
	public void createPartControl(Composite parent, final ILoginContributionContainer container) {
		waitForP2PNetworkManager();

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
		
		createLabel(rootComposite, "Peer port");
		final Text peerPortText = new Text(rootComposite, SWT.BORDER);
		peerPortText.setText(String.valueOf(peerPort));
		peerPortText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		peerPortText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				try {
					peerPort = Integer.valueOf(peerPortText.getText());
					container.changed();
					
					updateMessages(container);
				} catch( Throwable t ) {
				}
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

		createLabel(rootComposite, "Rendevous Address");
		final Text rendevousAddressText = new Text(rootComposite, SWT.BORDER);
		rendevousAddressText.setText(rendevousAddress);
		rendevousAddressText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		rendevousAddressText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				rendevousAddress = rendevousAddressText.getText();

				container.changed();
			}
		});

		final Button isRendevousActiveButton = new Button(rootComposite, SWT.CHECK);
		isRendevousActiveButton.setText("Is rendevous (own IP is " + determineOwnIPAddress() + ")");
		isRendevousActiveButton.setSelection(rendevousActive);
		isRendevousActiveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				rendevousActive = isRendevousActiveButton.getSelection();

				container.changed();
			}
		});
	}

	private String determineOwnIPAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			LOG.error("Could not determine own ip address");
			return "unknown";
		}
	}

	private void updateMessages(ILoginContributionContainer container) {
		if (Strings.isNullOrEmpty(peerName)) {
			container.setErrorMessage("Peer name must be specified");
		} else if (Strings.isNullOrEmpty(groupName)) {
			container.setErrorMessage("Peer group name must be specified");
		} else if (peerPort <= 1023 || peerPort > 65535) {
			container.setErrorMessage("Port must be between 1024 and 65535");
		} else if( !tryToURIIfNotNull(rendevousAddress)) {
			container.setErrorMessage("Rendevous address is invalid");
		} else {
			container.setErrorMessage(null);
		}
	}

	private static boolean tryToURIIfNotNull(String address) {
		if(Strings.isNullOrEmpty(address)) {
			return true;
		}
		
		try {
			new URI(ADDRESS_PREFIX + address);
		} catch (URISyntaxException e) {
			return false;
		}
		
		return true;
	}


	private static void createLabel(Composite rootComposite, String text) {
		Label peerNameLabel = new Label(rootComposite, SWT.NONE);
		peerNameLabel.setText(text);
	}

	@Override
	public void dispose() {
	}

	@Override
	public Map<String, String> onSave() {
		Map<String, String> saved = Maps.newHashMap();
		saved.put(PEER_NAME_SYS_PROPERTY, peerName);
		saved.put(PEER_PORT_SYS_PROPERTY, String.valueOf(peerPort));
		saved.put(PEER_GROUP_NAME_SYS_PROPERTY, groupName);
		saved.put(RENDEVOUS_ADDRESS_SYS_PROPERTY, rendevousAddress);
		saved.put(RENDEVOUS_ACTIVE_SYS_PROPERTY, String.valueOf(rendevousActive));
		return saved;
	}

	@Override
	public boolean onFinish() {
		waitForP2PNetworkManager();

		IP2PNetworkManager networkManager = RCPP2PNewPlugIn.getP2PNetworkManager();
		String currentPeerName = networkManager.getLocalPeerName();
		String currentGroupName = networkManager.getLocalPeerGroupName();
		URI currentRendevousAddressURI = networkManager.getRendevousPeerAddress();
		String currentRendevousAddress = currentRendevousAddressURI != null ? currentRendevousAddressURI.getHost() : "";
		int currentPort = networkManager.getPort();
		boolean currentIsRendevousActive = networkManager.isRendevousPeer();

		if (!peerName.equals(currentPeerName) || currentPort != peerPort || !groupName.equals(currentGroupName) || !currentRendevousAddress.equals(rendevousAddress) || currentIsRendevousActive != rendevousActive) {
			if (networkManager.isStarted()) {
				networkManager.stop();
			}

			networkManager.setLocalPeerGroupName(groupName);
			networkManager.setLocalPeerName(peerName);
			networkManager.setRendevousPeerAddress(toURI(rendevousAddress));
			networkManager.setRendevousPeer(rendevousActive);
			networkManager.setPort(peerPort);

			return tryStartNetwork(networkManager);
		}
		return true;
	}

	private static URI toURI(String address) {
		if( Strings.isNullOrEmpty(address)) {
			return null;
		}
		
		try {
			return new URI(ADDRESS_PREFIX + address);
		} catch (URISyntaxException e) {
			LOG.error("Could not transform address '" + ADDRESS_PREFIX + address + "' to URI", e);
			return null;
		}
	}

	private static void waitForP2PNetworkManager() {
		while (RCPP2PNewPlugIn.getP2PNetworkManager() == null) {
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

	@Override
	public int getPriority() {
		return 0;
	}
}
