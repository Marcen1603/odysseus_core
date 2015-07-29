package de.uniol.inf.is.odysseus.iql.basic.postprocessor;

import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.GeneratedMetamodel;
import org.eclipse.xtext.xtext.ecoreInference.IXtext2EcorePostProcessor;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;

@SuppressWarnings("restriction")
public abstract class AbstractIQLPostProcessor implements IXtext2EcorePostProcessor {

	@Override
	public void process(GeneratedMetamodel metamodel) {
		System.out.println("fss");
		for (IQLClass c : EcoreUtil2.getAllContentsOfType(metamodel, IQLClass.class)) {
			System.out.println(c);
		}
	}

}
