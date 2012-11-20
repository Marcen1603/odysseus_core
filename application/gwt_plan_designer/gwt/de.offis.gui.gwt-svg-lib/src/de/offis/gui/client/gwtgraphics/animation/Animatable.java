/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.offis.gui.client.gwtgraphics.animation;

/**
 * Classes implementing this interface can be animated by using the Animate
 * class.
 *
 * @author Henri Kerola / IT Mill Ltd
 *
 */
public interface Animatable {

        /**
         * Set the value of a numeric property.
         *
         * @param property
         *            the property to be set
         * @param value
         *            the value
         */
        public void setPropertyDouble(String property, double value);
}

