package de.uniol.inf.is.odysseus.classification;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.classification.objects.ObjectRuleRegistry;
import de.uniol.inf.is.odysseus.classification.objects.rules.ConcaveObjRule;
import de.uniol.inf.is.odysseus.classification.objects.rules.CornerObjRule;
import de.uniol.inf.is.odysseus.classification.objects.rules.RoundObjRule;
import de.uniol.inf.is.odysseus.classification.objects.rules.StraightObjRule;
import de.uniol.inf.is.odysseus.classification.objects.rules.VFormObjRule;

/**
 * @author Alexander Funk <alexander.funk@uni-oldenburg.de>
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Activator implements BundleActivator {

    private static BundleContext context;

    static BundleContext getContext() {
        return Activator.context;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public void start(final BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;
        ObjectRuleRegistry.registerObjRule(new ConcaveObjRule());
        ObjectRuleRegistry.registerObjRule(new CornerObjRule());
        ObjectRuleRegistry.registerObjRule(new RoundObjRule());
        ObjectRuleRegistry.registerObjRule(new StraightObjRule());
        ObjectRuleRegistry.registerObjRule(new VFormObjRule());
    }

    /*
     * (non-Javadoc)
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(final BundleContext bundleContext) throws Exception {
        Activator.context = null;
    }

}
