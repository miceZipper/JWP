
package com.micezipper.jwp.printmethods;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.List;

/**
 *
 * @author miceZipper
 * @param <T>
 */

public abstract class PrintMethod<T> implements Serializable{

    protected T printer;
    protected boolean available = false;

    public abstract List<String> getPrintersNames();
    public abstract void print(ByteBuffer outputByteBuffer);
    public abstract void setPrinter(String printerName);
    public abstract String getName();
    
    public boolean isAvailable(){
        return available;
    }
}
