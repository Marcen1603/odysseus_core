package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.rcp.dashboard.desc.DashboardPartDescriptor;

public class XMLDashboardPartHandler implements IDashboardPartHandler {

	private static final Logger LOG = LoggerFactory.getLogger(XMLDashboardPartHandler.class);
	
	@Override
	public void save(IDashboardPart part, IFile to) {
		Class<? extends IDashboardPart> partClass = part.getClass();
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("DashboardPart");
			rootElement.setAttribute("class", partClass.toString());
			rootElement.setAttribute("queryFile", part.getQueryFile().getFullPath().toString());
			
			Configuration config = part.getConfiguration();
			for( String name : config.getNames()) {
				Setting<?> setting = config.getSetting(name);
				
				Element settingElement = doc.createElement(name);
				settingElement.appendChild(doc.createTextNode(setting.get().toString()));
				rootElement.appendChild(settingElement);
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(to.getLocation().toFile());
			
			transformer.transform(source, result);
		
		} catch (ParserConfigurationException e) {
			LOG.error("Could no save DashboardPart " + part.getClass() + " to file " + to.getName(), e);
		} catch (TransformerConfigurationException e) {
			LOG.error("Could no save DashboardPart " + part.getClass() + " to file " + to.getName(), e);
		} catch (TransformerException e) {
			LOG.error("Could no save DashboardPart " + part.getClass() + " to file " + to.getName(), e);
		}
	}

	@Override
	public IDashboardPart load(IFile from) {
		return null;
	}

	@SuppressWarnings("unused")
	private static Optional<DashboardPartDescriptor> getDescriptorFromClass( Class<? extends IDashboardPart> clazz ) {
		List<String> names = DashboardPartRegistry.getDashboardPartNames();
		
		for( String name : names ) {
			Optional<Class<? extends IDashboardPart>> desc = DashboardPartRegistry.getDashboardPartClass(name);
			if( desc.isPresent() && desc.get().equals(clazz)) {
				return DashboardPartRegistry.getDashboardPartDescriptor(name);
			}
		}
		
		return Optional.absent();
	}
}
