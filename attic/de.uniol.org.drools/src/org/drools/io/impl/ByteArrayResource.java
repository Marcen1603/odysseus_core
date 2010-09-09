package org.drools.io.impl;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import org.drools.io.InternalResource;
import org.drools.io.Resource;

public class ByteArrayResource extends BaseResource
    implements
    InternalResource {

    private byte[] bytes;

    public ByteArrayResource(byte[] bytes) {
        if ( bytes == null ) {
            throw new IllegalArgumentException( "bytes cannot be null" );
        }
        this.bytes = bytes;
    }

    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream( this.bytes );
    }
    
    public Reader getReader() throws IOException {
        return new InputStreamReader( getInputStream() );
    }    
    
    public boolean hasURL() {
        return false;
    }

    public URL getURL() throws IOException {
        throw new FileNotFoundException( "byte[] cannot be resolved to URL" );
    }
    
    public long getLastModified() {
        throw new IllegalStateException( "reader does have a modified date" );
    }    
    
    public long getLastRead() {
        throw new IllegalStateException( "reader does have a modified date" );
    }      
    
    public boolean isDirectory() {
        return false;
    }

    public Collection<Resource> listResources() {
        throw new RuntimeException( "This Resource cannot be listed, or is not a directory" );
    }      

    @Override
	public boolean equals(Object object) {
        return (object == this || (object instanceof ByteArrayResource && Arrays.equals( ((ByteArrayResource) object).bytes,
                                                                                         this.bytes )));
    }

    @Override
	public int hashCode() {
        return (byte[].class.hashCode() * 29 * this.bytes.length);
    }
    
    @Override
	public String toString() {
        return "[ByteArrayResource resource=" + this.bytes + "]";
    }




}
