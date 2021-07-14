package com.lenin.warpstonemod.common.math;

public final class MathUtil {

    /*
    clamp works by getting the lowest value between either the entered value or the maximum, and then getting the greatest value
    from either the maximum or the value
     */
    public static int clamp (int value, int minimum, int maximum){
        return Math.max(minimum, Math.min(maximum, value));
    }

    public static float clamp (float value, float minimum, float maximum){
        return Math.max(minimum, Math.min(maximum, value));
    }

    public static double clamp (double value, double minimum, double maximum){
        return Math.max(minimum, Math.min(maximum, value));
    }

}