package de.uniol.inf.is.odysseus.classification.objects;

import java.util.HashMap;
import java.util.Map.Entry;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * @author Alexander Funk <alexander.funk@uni-oldenburg.de>
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ObjectRuleRegistry {

    private static final HashMap<IObjectType, IObjectRule> objRules = new HashMap<IObjectType, IObjectRule>();

    public static void registerObjRule(final IObjectRule objRule) {
        ObjectRuleRegistry.objRules.put(objRule.getType(), objRule);
    }

    public static Geometry getPredictedPolygon(final Geometry segment, final IObjectType type) {
        if (ObjectRuleRegistry.objRules.get(type) == null) {
            return segment.getFactory().createLineString(new Coordinate[0]);
        }

        return ObjectRuleRegistry.objRules.get(type).getPredictedPolygon(segment);
    }

    public static TypeDetails getTypeDetails(final Geometry segment) {
        final TypeDetails details = new TypeDetails();

        for (final Entry<IObjectType, IObjectRule> objRule : ObjectRuleRegistry.objRules.entrySet()) {
            details.addTypeAffinity(objRule.getValue().getType(), objRule.getValue().getTypeAffinity(segment));
        }
        details.normalize();
        
        return details;
    }
}
