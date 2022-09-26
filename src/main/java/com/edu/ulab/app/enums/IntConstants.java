package com.edu.ulab.app.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum IntConstants {
    PERSON_UNREAL_AGE_INT(0),
    BOOK_UNREAL_PAGE_QTY(0);
    private final int digits;

    IntConstants(int digits)
    {
        this.digits = digits;
    }
}
