package de.uniol.inf.is.odysseus.rest2.server.api;

public class ApiException extends Exception{
    private int code;
    public ApiException (int code, String msg) {
        super(msg);
        this.code = code;
    }
}
