package com.ssa.constant;

import com.ssa.response.Status;

public class StatusConstants {

    public static final String success_code = "1000";
    public static final String success_header = "Success";
    public static final String success_description = "The request was successfull.";

    public static final String invalid_code = "400";
    public static final String invalid_header = "Invalid details";
    public static final String invalid_description = "The request was Invalid.";

    public static final Status success() {
        return new Status(success_code, success_header, success_description);
    }
    public static final Status invalid() {
        return new Status(invalid_code, invalid_header, invalid_description);
    }

}