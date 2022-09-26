package com.edu.ulab.app.service;

import com.edu.ulab.app.enums.IntConstants;

public interface CheckDifferentData {
    public default int checkValidHumanAge(int someAge){
        return (someAge > 5 && someAge < 125) ? someAge : IntConstants.PERSON_UNREAL_AGE_INT.getDigits();
    }
}
