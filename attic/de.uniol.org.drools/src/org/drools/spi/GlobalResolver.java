package org.drools.spi;

public interface GlobalResolver {
    public Object resolveGlobal(String identifier);

    public void setGlobal(String identifier,
                          Object value);
}
