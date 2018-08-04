package com.asekulsk.nepichain.util;

import java.security.*;
import java.util.Base64;

/**
 * String utility helper class.
 * @author Andreas Sekulski
 */
public class StringUtil {

    /**
     * Get string representation from key.
     * @param key Key to convert as String.
     * @return String representation from key.
     */
    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}

