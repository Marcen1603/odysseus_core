package de.uniol.inf.is.odysseus.iql.basic.linking;



import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.IIQLTypeFactory;



public class IQLResourceFactory extends XtextResourceFactory {
	
	@Inject
	private IIQLTypeFactory typeFactory;

	@Inject
	public IQLResourceFactory(Provider<XtextResource> resourceProvider) {
		super(resourceProvider);
	}


	@Override
	public Resource createResource(URI uri) {	
		if (typeFactory.isSystemFile(uriToFileName(uri))) {
			IQLFile systemFile = typeFactory.getSystemFile(uriToFileName(uri));
			IQLLinkingResource resource = new IQLLinkingResource();
			resource.setSystemFile(systemFile);
			resource.setURI(uri);
			resource.getContents().add(systemFile);
			return resource;		
		}
		return super.createResource(uri);
	}
	
	private String uriToFileName(URI uri) {
		return uri.trimFileExtension().segment(0);
	}
}
