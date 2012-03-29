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
package org.apache.commons.math3.transform;

/**
 * This enumeration defines the various types of normalizations that can be
 * applied to discrete sine transforms (DST). The exact definition of these
 * normalizations is detailed below.
 *
 * @see FastSineTransformer
 * @version $Id: DstNormalization.java 1244107 2012-02-14 16:17:55Z erans $
 * @since 3.0
 */
public enum DstNormalization {
    /**
     * <p>
     * Should be passed to the constructor of {@link FastSineTransformer} to
     * use the <em>standard</em> normalization convention. The standard DST-I
     * normalization convention is defined as follows
     * <ul>
     * <li>forward transform: y<sub>n</sub> = &sum;<sub>k=0</sub><sup>N-1</sup>
     * x<sub>k</sub> sin(&pi; nk / N),</li>
     * <li>inverse transform: x<sub>k</sub> = (2 / N)
     * &sum;<sub>n=0</sub><sup>N-1</sup> y<sub>n</sub> sin(&pi; nk / N),</li>
     * </ul>
     * where N is the size of the data sample, and x<sub>0</sub> = 0.
     * </p>
     */
    STANDARD_DST_I,

    /**
     * <p>
     * Should be passed to the constructor of {@link FastSineTransformer} to
     * use the <em>orthogonal</em> normalization convention. The orthogonal
     * DCT-I normalization convention is defined as follows
     * <ul>
     * <li>Forward transform: y<sub>n</sub> = &radic;(2 / N)
     * &sum;<sub>k=0</sub><sup>N-1</sup> x<sub>k</sub> sin(&pi; nk / N),</li>
     * <li>Inverse transform: x<sub>k</sub> = &radic;(2 / N)
     * &sum;<sub>n=0</sub><sup>N-1</sup> y<sub>n</sub> sin(&pi; nk / N),</li>
     * </ul>
     * which makes the transform orthogonal. N is the size of the data sample,
     * and x<sub>0</sub> = 0.
     * </p>
     */
    ORTHOGONAL_DST_I
}
