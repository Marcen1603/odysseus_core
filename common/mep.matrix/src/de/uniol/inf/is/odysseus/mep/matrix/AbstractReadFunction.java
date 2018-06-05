/**********************************************************************************
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mep.matrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
abstract public class AbstractReadFunction<T> extends AbstractFunction<T> {

    /**
     *
     */
    private static final long serialVersionUID = -4388346024131508003L;
    private static final Logger LOG = LoggerFactory.getLogger(AbstractReadFunction.class);

    /**
     * @param symbol
     * @param arity
     * @param acceptedTypes
     * @param returnType
     */
    public AbstractReadFunction(final String symbol, final int arity, final SDFDatatype[][] acceptedTypes, final SDFDatatype returnType) {
        super(symbol, arity, acceptedTypes, returnType);
    }

    protected static double[][] getValueInternal(final String path, final String delimiter, final int[] elements) {
        final File file = new File(path);
        final List<double[]> resultList = new LinkedList<>();
        if (file.canRead()) {
            FileInputStream stream;
            try {
                stream = new FileInputStream(file);
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        final String[] stringValues = line.split(Pattern.quote("" + delimiter));
                        if (elements != null) {
                            final double[] values = new double[elements.length];
                            for (int i = 0; i < elements.length; i++) {
                                values[i] = Double.parseDouble(stringValues[elements[i]].trim());
                            }
                            resultList.add(values);
                        }
                        else {
                            final double[] values = new double[stringValues.length];
                            for (int i = 0; i < stringValues.length; i++) {
                                values[i] = Double.parseDouble(stringValues[i].trim());
                            }
                            resultList.add(values);
                        }
                    }
                }
                catch (final IOException e) {
                    AbstractReadFunction.LOG.warn(e.getMessage(), e);
                }
            }
            catch (final FileNotFoundException e) {
                AbstractReadFunction.LOG.warn(e.getMessage(), e);
            }
        }
        final double[][] result = new double[resultList.size()][resultList.get(0).length];
        final Iterator<double[]> iter = resultList.iterator();
        int i = 0;
        while (iter.hasNext()) {
            result[i] = iter.next();
            i++;
        }

        return result;
    }

}
