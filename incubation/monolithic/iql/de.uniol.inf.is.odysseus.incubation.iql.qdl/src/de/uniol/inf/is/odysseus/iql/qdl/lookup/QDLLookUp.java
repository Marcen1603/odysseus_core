package de.uniol.inf.is.odysseus.iql.qdl.lookup;

import java.util.Collection;
import java.util.HashSet;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.resource.IResourceDescription;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.lookup.AbstractIQLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLModel;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeExtensionsFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeUtils;

public class QDLLookUp extends AbstractIQLLookUp<QDLTypeFactory, QDLTypeExtensionsFactory, QDLTypeUtils>{


	@Inject
	public QDLLookUp(QDLTypeFactory typeFactory, QDLTypeExtensionsFactory typeOperatorsFactory, QDLTypeUtils typeUtils) {
		super(typeFactory, typeOperatorsFactory, typeUtils);
	}

	@Override
	protected Collection<IQLModel> getAllFiles(Resource context) {
		Collection<IQLModel> files = new HashSet<>();
		for (IResourceDescription res : resources.getAllResourceDescriptions()) {
			Resource r = EcoreUtil2.getResource(context, res.getURI().toString());
			if (r.getContents().size() > 0) {
				EObject obj = r.getContents().get(0);
				if (obj instanceof QDLModel) {
					files.add((IQLModel)obj);
				}
			}
		}
		return files;
	}
}
