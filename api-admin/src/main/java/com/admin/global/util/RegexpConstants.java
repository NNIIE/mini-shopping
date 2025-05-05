package com.admin.global.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RegexpConstants {

    public static final String EMAIL_REGEXP = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$";
    public static final String SPECIAL_CHARACTERS_REGEXP = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{10,16}";

}
