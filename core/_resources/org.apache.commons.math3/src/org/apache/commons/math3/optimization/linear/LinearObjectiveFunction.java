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

package org.apache.commons.math3.optimization.linear;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;

/**
 * An objective function for a linear optimization problem.
 * <p>
 * A linear objective function has one the form:
 * <pre>
 * c<sub>1</sub>x<sub>1</sub> + ... c<sub>n</sub>x<sub>n</sub> + d
 * </pre>
 * The c<sub>i</sub> and d are the coefficients of the equation,
 * the x<sub>i</sub> are the coordinates of the current point.
 * </p>
 * @version $Id: LinearObjectiveFunction.java 1244107 2012-02-14 16:17:55Z erans $
 * @since 2.0
 */
public class LinearObjectiveFunction implements Serializable {

    /** Serializable version identifier. */
    private static final long serialVersionUID = -4531815507568396090L;

    /** Coefficients of the constraint (c<sub>i</sub>). */
    private final transient RealVector coefficients;

    /** Constant term of the linear equation. */
    private final double constantTerm;

    /**
     * @param coefficients The coefficients for the linear equation being optimized
     * @param constantTerm The constant term of the linear equation
     */
    public LinearObjectiveFunction(double[] coefficients, double constantTerm) {
        this(new ArrayRealVector(coefficients), constantTerm);
    }

    /**
     * @param coefficients The coefficients for the linear equation being optimized
     * @param constantTerm The constant term of the linear equation
     */
    public LinearObjectiveFunction(RealVector coefficients, double constantTerm) {
        this.coefficients = coefficients;
        this.constantTerm = constantTerm;
    }

    /**
     * Get the coefficients of the linear equation being optimized.
     * @return coefficients of the linear equation being optimized
     */
    public RealVector getCoefficients() {
        return coefficients;
    }

    /**
     * Get the constant of the linear equation being optimized.
     * @return constant of the linear equation being optimized
     */
    public double getConstantTerm() {
        return constantTerm;
    }

    /**
     * Compute the value of the linear equation at the current point
     * @param point point at which linear equation must be evaluated
     * @return value of the linear equation at the current point
     */
    public double getValue(final double[] point) {
        return coefficients.dotProduct(new ArrayRealVector(point, false)) + constantTerm;
    }

    /**
     * Compute the value of the linear equation at the current point
     * @param point point at which linear equation must be evaluated
     * @return value of the linear equation at the current point
     */
    public double getValue(final RealVector point) {
        return coefficients.dotProduct(point) + constantTerm;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {

      if (this == other) {
        return true;
      }

      if (other instanceof LinearObjectiveFunction) {
          LinearObjectiveFunction rhs = (LinearObjectiveFunction) other;
          return (constantTerm == rhs.constantTerm) && coefficients.equals(rhs.coefficients);
      }

      return false;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Double.valueOf(constantTerm).hashCode() ^ coefficients.hashCode();
    }

    /** Serialize the instance.
     * @param oos stream where object should be written
     * @throws IOException if object cannot be written to stream
     */
    private void writeObject(ObjectOutputStream oos)
        throws IOException {
        oos.defaultWriteObject();
        MatrixUtils.serializeRealVector(coefficients, oos);
    }

    /** Deserialize the instance.
     * @param ois stream from which the object should be read
     * @throws ClassNotFoundException if a class in the stream cannot be found
     * @throws IOException if object cannot be read from the stream
     */
    private void readObject(ObjectInputStream ois)
      throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        MatrixUtils.deserializeRealVector(this, "coefficients", ois);
    }

}
