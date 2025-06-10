package com.support.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RegexpConstants {

    public static final String EMAIL_REGEXP = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$";
    public static final String ENGLISH_KOREAN_REGEXP = "^[가-힣a-zA-Z]+$";
    public static final String SPECIAL_CHARACTERS_REGEXP = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{10,16}";
    public static final String PHONE_NUMBER_REGEXP = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$";


}

