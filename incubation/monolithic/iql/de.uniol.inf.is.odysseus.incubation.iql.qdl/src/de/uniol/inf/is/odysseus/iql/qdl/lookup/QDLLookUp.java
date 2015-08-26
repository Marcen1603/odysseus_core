package de.uniol.inf.is.odysseus.iql.qdl.lookup;


import javax.inject.Inject;


import de.uniol.inf.is.odysseus.iql.basic.lookup.AbstractIQLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.typing.factory.IQDLTypeFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.typeextension.IQDLTypeExtensionsFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;

public class QDLLookUp extends AbstractIQLLookUp<IQDLTypeFactory, IQDLTypeExtensionsFactory, IQDLTypeUtils> implements IQDLLookUp{


	@Inject
	public QDLLookUp(IQDLTypeFactory typeFactory, IQDLTypeExtensionsFactory typeOperatorsFactory, IQDLTypeUtils typeUtils) {
		super(typeFactory, typeOperatorsFactory, typeUtils);
	}

//	@Override
//	protected Collection<IQLModel> getAllFiles(Resource context) {
//		Collection<IQLModel> files = new HashSet<>();
//		for (IResourceDescription res : resources.getAllResourceDescriptions()) {
//			Resource r = EcoreUtil2.getResource(context, res.getURI().toString());
//			if (r.getContents().size() > 0) {
//				EObject obj = r.getContents().get(0);
//				if (obj instanceof QDLModel) {
//					files.add((IQLModel)obj);
//				}
//			}
//		}
//		return files;
//	}
}
