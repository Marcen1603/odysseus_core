/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.analysis.BivariateFunction;

/**
 * Interface representing a bivariate real interpolating function where the
 * sample points must be specified on a regular grid.
 *
 * @version $Id: BivariateGridInterpolator.java 1244107 2012-02-14 16:17:55Z erans $
 */
public interface BivariateGridInterpolator {
    /**
     * Compute an interpolating function for the dataset.
     *
     * @param xval All the x-coordinates of the interpolation points, sorted
     * in increasing order.
     * @param yval All the y-coordinates of the interpolation points, sorted
     * in increasing order.
     * @param fval The values of the interpolation points on all the grid knots:
     * {@code fval[i][j] = f(xval[i], yval[j])}.
     * @return a function which interpolates the dataset.
     * @throws org.apache.commons.math3.exception.NoDataException if any of
     * the arrays has zero length.
     * @throws org.apache.commons.math3.exception.DimensionMismatchException
     * if the array lengths are inconsistent.
     */
    BivariateFunction interpolate(double[] xval, double[] yval,
                                      double[][] fval);
}
