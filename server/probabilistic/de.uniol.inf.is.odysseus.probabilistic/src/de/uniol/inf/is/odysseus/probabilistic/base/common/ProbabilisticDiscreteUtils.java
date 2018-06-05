/*
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.base.common;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public final class ProbabilisticDiscreteUtils {
    /**
     * Expand all possible worlds from the given probabilistic attributes.
     * 
     * @param input
     *            The tuple
     * @param probabilisticAttributePos
     *            The positions of the probabilistic attributes
     * @return The possible worlds based on the values of the probabilistic
     *         attributes
     */
    // public static Object[][] getWorlds(final Tuple<?> input, final int[]
    // probabilisticAttributePos) {
    // Objects.requireNonNull(input);
    // Objects.requireNonNull(probabilisticAttributePos);
    //
    // final Iterator<?>[] attributeIters = new
    // Iterator<?>[probabilisticAttributePos.length];
    // int worldNum = 1;
    // for (int i = 0; i < probabilisticAttributePos.length; i++) {
    // final AbstractProbabilisticValue<?> attribute =
    // (AbstractProbabilisticValue<?>)
    // input.getAttribute(probabilisticAttributePos[i]);
    // worldNum *= attribute.getValues().size();
    // attributeIters[i] = attribute.getValues().entrySet().iterator();
    // }
    //
    // // Create all possible worlds
    // final Object[][] worlds = new
    // Object[worldNum][probabilisticAttributePos.length];
    // double instances = 1.0;
    // for (int i = 0; i < probabilisticAttributePos.length; i++) {
    // int world = 0;
    // final AbstractProbabilisticValue<?> attribute =
    // (AbstractProbabilisticValue<?>)
    // input.getAttribute(probabilisticAttributePos[i]);
    // int num = (int) (worlds.length / (attribute.getValues().size() *
    // instances));
    // while (num > 0) {
    // final Iterator<?> iter = attribute.getValues().entrySet().iterator();
    // while (iter.hasNext()) {
    // @SuppressWarnings("unchecked")
    // final Entry<?, Double> entry = ((Map.Entry<?, Double>) iter.next());
    // for (int j = 0; j < instances; j++) {
    // worlds[world][i] = entry.getKey();
    // world++;
    // }
    // }
    // num--;
    // }
    // instances *= attribute.getValues().size();
    // }
    // return worlds;
    // }

    /**
     * Private constructor.
     */
    private ProbabilisticDiscreteUtils() {
        throw new UnsupportedOperationException();
    }
}
