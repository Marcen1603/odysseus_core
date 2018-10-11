package de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartQueryTextProvider;

public class GraphQueryFileProvider implements IDashboardPartQueryTextProvider {

	private static final Logger LOG = LoggerFactory.getLogger(GraphQueryFileProvider.class);
	
	private final IFile graphFile;
	
	public GraphQueryFileProvider(IFile graphFile) {
		// Preconditions.checkNotNull(graphFile, "Graph file must not be null!");
		// Preconditions.checkArgument(graphFile.getFileExtension().equals("grp"), "Grapg file must end with 'grp'");
		
		this.graphFile = graphFile;
	}
	
	public IFile getGraphFile() {
		return graphFile;
	}
	
	@Override
	public ImmutableList<String> getQueryText() {
		try {
			InputStream is = graphFile.getContents();
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			Element root = doc.getDocumentElement();
			
			Optional<Node> optPqlNode = getPQLNode(root);
			if( optPqlNode.isPresent() ) {
				Node pqlNode = optPqlNode.get();
				Node pqlDataNode = pqlNode.getFirstChild();
				String pqlText = pqlDataNode.getNodeValue();
				
				pqlText = "#PARSER PQL" + System.lineSeparator() + "#TRANSCFG Standard" + System.lineSeparator() + "#RUNQUERY" + System.lineSeparator() + pqlText;
				
				return ImmutableList.of(pqlText);
				
			} 
			throw new IOException("Could not find pql-Node in graph file");
		} catch (CoreException | ParserConfigurationException | SAXException | IOException e) {
			LOG.error("Could not get pql-Statement from graph file", e);
			return ImmutableList.of();
		}
	}

	private static Optional<Node> getPQLNode(Element root) {
		NodeList childNodes = root.getChildNodes();
		for( int i = 0; i < childNodes.getLength(); i++ ) {
			Node childNode = childNodes.item(i);
			if( "pql".equalsIgnoreCase(childNode.getNodeName())) {
				return Optional.of(childNode);
			}
		}
		return Optional.absent();
	}

}
