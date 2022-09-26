package com.edu.ulab.app.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum LongConstants {
    USER_ID_NOT_FOUND_LONG(0L),
    SMTHNG_GOES_WRONG_CONNECTION_TO_DB(-1L),
    BOOK_ID_NOT_FOUND_LONG(0L),
    BOOK_UNREAL_PAGE_QTY_LONG(0L);
    private final long digits;

    LongConstants(long digits)
    {
        this.digits = digits;
    }
}
