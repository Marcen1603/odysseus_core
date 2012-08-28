package de.uniol.inf.is.odysseus.classification;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.classification.objects.ClassifyObject;
import de.uniol.inf.is.odysseus.classification.segmentation.IEPFSegmentation;
import de.uniol.inf.is.odysseus.classification.segmentation.LTSegmentation;
import de.uniol.inf.is.odysseus.classification.segmentation.SEFSegmentation;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.server.mep.IFunctionProvider;

/**
 * @author Alexander Funk <alexander.funk@uni-oldenburg.de>
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ClassificationFunctionProvider implements IFunctionProvider {

    @Override
    public List<IFunction<?>> getFunctions() {
        if (ClassificationDatatypeProvider.datadictionary == null) {
            return new ArrayList<IFunction<?>>();
        }
        final List<IFunction<?>> functions = new ArrayList<IFunction<?>>();

        // Add segmentation functions
        functions.add(new IEPFSegmentation());
        functions.add(new LTSegmentation());
        functions.add(new SEFSegmentation());
        // Add classification function
        functions.add(new ClassifyObject());

        return functions;
    }

}
