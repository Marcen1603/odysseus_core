package de.uniol.inf.is.odysseus.salsa.metadata.impl;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.salsa.metadata.Quality;

public class QualityImpl implements Quality, Cloneable, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1780818856411719102L;

    public QualityImpl() {
        // TODO Auto-generated method stub
    }

    public QualityImpl(QualityImpl quality) {
        // TODO Auto-generated method stub
    }

    @Override
    public String csvToString() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String csvToString(boolean withMetada) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getCSVHeader() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Quality clone() {
        return new QualityImpl(this);
    }

    @Override
    public int compareTo(Quality quality) {
        // TODO Auto-generated method stub
        return 0;
    }

}
