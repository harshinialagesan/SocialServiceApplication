package com.ssa.exceptions;

public class DataNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 182785148209014023L;

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException() {
        super("No Data Found.");
    }


}
