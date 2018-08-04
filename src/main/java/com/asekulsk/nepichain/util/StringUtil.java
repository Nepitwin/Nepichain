package com.asekulsk.nepichain.util;

import java.security.*;
import java.util.Base64;

public class StringUtil {

    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

}

