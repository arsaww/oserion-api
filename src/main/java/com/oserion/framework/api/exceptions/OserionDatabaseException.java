package com.oserion.framework.api.exceptions;

/**
 * Created by Arsaww on 12/17/2015.
 */
public class OserionDatabaseException extends Exception {

    public OserionDatabaseException(){super();}
    public OserionDatabaseException(String message){super(message);}
    public OserionDatabaseException(Throwable cause){super(cause);}
    public OserionDatabaseException(String message, Throwable cause){super(message, cause);}
    public OserionDatabaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {super(message, cause, enableSuppression, writableStackTrace);}

}
