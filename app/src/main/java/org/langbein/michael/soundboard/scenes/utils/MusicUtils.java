package org.langbein.michael.soundboard.scenes.utils;

/**
 * Created by michael on 28.01.18.
 */

public class MusicUtils {
    public static float getNthTone(float base, float n) {
        return (float) (base * Math.pow(2.0, (n/12.0)));
    }
}
