package de.uniol.inf.is.odysseus.net.connect.select.group;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNetComponentAdapter;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.config.OdysseusNetConfiguration;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionSelector;

public class GroupSelectorComponent extends OdysseusNetComponentAdapter implements IOdysseusNodeConnectionSelector {

	private static final Logger LOG = LoggerFactory.getLogger(GroupSelectorComponent.class);
	
	private static final String NODE_GROUP_PROPERTY_KEY = "nodeGroup";
	private static final String NODE_GROUP_CONFIGURATION_KEY = "net.node.group";
	
	private static final String DEFAULT_NODE_GROUP = "OdysseusGroup";
	private static final String NODE_GROUP_WILDCARD = "*";
	
	private String ownNodeGroup;
	
	@Override
	public void init(IOdysseusNode localNode) throws OdysseusNetException {
		ownNodeGroup = determineOwnNodeGroup();
		
		localNode.addProperty(NODE_GROUP_PROPERTY_KEY, ownNodeGroup);
		LOG.info("Local node is in node group {}", ownNodeGroup);
	}

	private static String determineOwnNodeGroup() throws OdysseusNetException {
		String ownNodeGroup = OdysseusNetConfiguration.get(NODE_GROUP_CONFIGURATION_KEY, DEFAULT_NODE_GROUP);
		if( Strings.isNullOrEmpty(ownNodeGroup)) {
			throw new OdysseusNetException("Node group must not be null or empty!");
		}
		
		return ownNodeGroup;
	}

	@Override
	public boolean doConnect(IOdysseusNode node) {
		Optional<String> optNodeGroup = node.getProperty(NODE_GROUP_PROPERTY_KEY);
		if( optNodeGroup.isPresent() ) {
			String nodeGroup = optNodeGroup.get().trim();
			LOG.info("Other node is in group {}", nodeGroup);
			if( NODE_GROUP_WILDCARD.equals(nodeGroup) || ownNodeGroup.equals(nodeGroup)) {
				LOG.debug("We want to connect to the node");
				return true;
			}
			LOG.debug("We do not connect to the node");
			return false;
		} 
		LOG.debug("Node has no info about its node group");
		return false;
	}

}
