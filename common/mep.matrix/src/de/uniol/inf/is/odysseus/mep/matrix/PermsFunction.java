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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.util.MathUtils;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class PermsFunction extends AbstractFunction<double[][]> {
    /**
     * 
     */
    private static final long serialVersionUID = 8679234269779879914L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.VECTORS };

    public PermsFunction() {
        super("perms", 1, ACC_TYPES, SDFDatatype.MATRIX_DOUBLE);
    }

    @Override
    public double[][] getValue() {
        double[] a = (double[]) this.getInputValue(0);
        return getValueInternal(a);
    }

    protected static double[][] getValueInternal(double[] a) {
        long factorial = MathUtils.factorial(a.length);
        List<double[]> out = new ArrayList<>((int) factorial);
        calcHeapPermutation(out, a, a.length);
        return out.toArray(new double[][] {});
    }

    private static void calcHeapPermutation(List<double[]> out, double[] value, int n) {
        if (n == 1) {
            out.add(value.clone());
        }
        else {
            for (int i = 0; i < n; i++) {
                calcHeapPermutation(out, value, n - 1);
                if (n % 2 == 1) {
                    double tmp = value[0];
                    value[0] = value[n - 1];
                    value[n - 1] = tmp;
                }
                else {
                    double tmp = value[i];
                    value[i] = value[n - 1];
                    value[n - 1] = tmp;
                }
            }
        }
    }

}