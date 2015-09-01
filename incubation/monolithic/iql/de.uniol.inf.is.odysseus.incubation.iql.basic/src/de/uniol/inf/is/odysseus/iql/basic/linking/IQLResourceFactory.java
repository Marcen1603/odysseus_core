package de.uniol.inf.is.odysseus.iql.basic.linking;



import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.IIQLTypeDictionary;



public class IQLResourceFactory extends XtextResourceFactory {
	
	@Inject
	private IIQLTypeDictionary typeDictionary;

	@Inject
	public IQLResourceFactory(Provider<XtextResource> resourceProvider) {
		super(resourceProvider);
	}


	@Override
	public Resource createResource(URI uri) {	
		if (typeDictionary.isSystemFile(uriToFileName(uri))) {
			IQLModel systemFile = typeDictionary.getSystemFile(uriToFileName(uri));
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
