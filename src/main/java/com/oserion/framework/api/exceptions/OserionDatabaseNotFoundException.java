package com.oserion.framework.api.exceptions;

/**
 * Created by Arsaww on 12/17/2015.
 */
public class OserionDatabaseNotFoundException extends OserionDatabaseException {

    public OserionDatabaseNotFoundException(){super();}
    public OserionDatabaseNotFoundException(String message){super(message);}
    public OserionDatabaseNotFoundException(Throwable cause){super(cause);}
    public OserionDatabaseNotFoundException(String message, Throwable cause){super(message, cause);}
    public OserionDatabaseNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {super(message, cause, enableSuppression, writableStackTrace);}

}
