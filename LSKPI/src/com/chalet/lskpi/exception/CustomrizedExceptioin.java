package com.chalet.lskpi.exception;

public class CustomrizedExceptioin extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public CustomrizedExceptioin() {
        super();
    }

    /**
     * @param message
     */
    public CustomrizedExceptioin(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public CustomrizedExceptioin(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public CustomrizedExceptioin(Throwable cause) {
        super(cause);
    }

}
