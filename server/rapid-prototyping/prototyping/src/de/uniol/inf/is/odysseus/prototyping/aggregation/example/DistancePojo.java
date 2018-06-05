/**********************************************************************************
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 *
 */
package de.uniol.inf.is.odysseus.prototyping.aggregation.example;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.prototyping.aggregation.Evaluate;
import de.uniol.inf.is.odysseus.prototyping.aggregation.Init;
import de.uniol.inf.is.odysseus.prototyping.aggregation.Merge;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class DistancePojo {
    private final static double RADIUS = 6367000.0;

    @Init(meta = false)
    public List<Position> init(final Object[] attr) {
        final Position pos = new Position(((Number) attr[0]).doubleValue(), ((Number) attr[1]).doubleValue());
        final List<Position> positions = new ArrayList<>();
        positions.add(pos);
        return positions;
    }

    @Merge(meta = false)
    public List<Position> merge(final List<Position> partial, final Object... attr) {
        final Position pos = new Position(((Number) attr[0]).doubleValue(), ((Number) attr[1]).doubleValue());
        partial.add(pos);
        return partial;
    }

    @Evaluate
    //@SuppressWarnings("static-method")
    public Double evaluate(final List<Position> partial) {
        double distance = 0.0;
        double tmpLat = partial.get(0).latitude;
        double tmpLng = partial.get(0).longitude;
        final double radian = Math.PI / 180;
        for (final Position pos : partial) {
            final double deltaLatitude = Math.sin((radian * (tmpLat - pos.latitude)) / 2);
            final double deltaLongitude = Math.sin((radian * (tmpLng - pos.longitude)) / 2);
            final double circleDistance = 2 * Math.asin(Math.min(1,
                    Math.sqrt((deltaLatitude * deltaLatitude) + (Math.cos(radian * tmpLat) * Math.cos(radian * pos.latitude) * deltaLongitude * deltaLongitude))));
            distance += Math.abs(DistancePojo.RADIUS * circleDistance);
            tmpLat = pos.latitude;
            tmpLng = pos.longitude;
        }
        return new Double(distance);
    }

    class Position {
        public final double latitude;
        public final double longitude;

        public Position(final double latitude, final double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

    }
}
