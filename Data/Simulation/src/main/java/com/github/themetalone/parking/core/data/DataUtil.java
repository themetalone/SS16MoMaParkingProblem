package com.github.themetalone.parking.core.data;

/**
 * Created by steff on 13.07.2016.
 */
public class DataUtil {

    /**
     * Converts a boolean array to a byte array
     * @param input boolean[]
     * @return byte[]
     * @link http://stackoverflow.com/users/1081110/david-wallace
     */
    public static byte[] toBytes(boolean[] input) {
        byte[] toReturn = new byte[input.length / 8];
        for (int entry = 0; entry < toReturn.length; entry++) {
            for (int bit = 0; bit < 8; bit++) {
                if (input[entry * 8 + bit]) {
                    toReturn[entry] |= (128 >> bit);
                }
            }
        }
        return toReturn;
    }
}