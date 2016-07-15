package com.github.themetalone.parking.core.data;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Util class for data manipulation
 * Created by steff on 13.07.2016.
 */
public class DataUtil {

    /**
     * Converts a boolean array to a byte array
     *
     * @param input boolean[]
     * @return byte[]
     * @link http://stackoverflow.com/users/1081110/david-wallace
     */
    public static byte[] toBytes(boolean[] input) {
        byte[] toReturn = new byte[1 + input.length / 8];
        input = turnBooleanArray(input);
        for (int entry = 0; entry < toReturn.length; entry++) {
            for (int bit = 0; bit < 8; bit++) {
                if (((entry * 8 + bit) < input.length) && input[entry * 8 + bit]) {
                    toReturn[entry] |= (128 >> bit);
                }
            }

        }
        return toReturn;
    }

    public static String booleanArrayToString(boolean[] array) {
        String result = "";
        for (boolean anArray : array) {
            result = (anArray ? "1" : "0") + result;
        }
        return result;
    }

    public static boolean[] turnBooleanArray(boolean[] array) {
        boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[array.length - 1 - i] = array[i];
        }
        return result;
    }

}
