/*
 * Copyright 2018
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.lujun.androidtagview;

import android.graphics.Color;

public class ColorFactory {

    /**
     *                      ============= --border color
     *  background color---||-  Text --||--text color
     *                     =============
     */

    private static final String BG_COLOR_ALPHA = "33";
    private static final String BD_COLOR_ALPHA = "88";

    private static final String RED = "F44336";
    private static final String LIGHTBLUE = "03A9F4";
    private static final String AMBER = "FFC107";
    private static final String ORANGE = "FF9800";
    private static final String YELLOW = "FFEB3B";
    private static final String LIME = "CDDC39";
    private static final String BLUE = "2196F3";
    private static final String INDIGO = "3F51B5";
    private static final String LIGHTGREEN = "8BC34A";
    private static final String GREY = "9E9E9E";
    private static final String DEEPPURPLE = "673AB7";
    private static final String TEAL = "009688";
    private static final String CYAN = "00BCD4";

    public enum PURE_COLOR{CYAN, TEAL}

    public static final int NONE = -1;
    public static final int RANDOM = 0;
    public static final int PURE_CYAN = 1;
    public static final int PURE_TEAL = 2;

    private static final int SHARP666666 = Color.parseColor("#FF666666");
    private static final int SHARP727272 = Color.parseColor("#FF727272");

    private static final String[] COLORS = new String[]{RED, LIGHTBLUE, AMBER, ORANGE, YELLOW,
            LIME, BLUE, INDIGO, LIGHTGREEN, GREY, DEEPPURPLE, TEAL, CYAN};

    public static int[] onRandomBuild(){
        int random = (int)(Math.random() * COLORS.length);
        int bgColor = Color.parseColor("#" + BG_COLOR_ALPHA + COLORS[random]);
        int bdColor = Color.parseColor("#" + BD_COLOR_ALPHA + COLORS[random]);
        int tColor = SHARP666666;
        return new int[]{bgColor, bdColor, tColor};
    }

    public static int[] onPureBuild(PURE_COLOR type){
        String color = type == PURE_COLOR.CYAN ? CYAN : TEAL;
        int bgColor = Color.parseColor("#" + BG_COLOR_ALPHA + color);
        int bdColor = Color.parseColor("#" + BD_COLOR_ALPHA + color);
        int tColor = SHARP727272;
        return new int[]{bgColor, bdColor, tColor};
    }

}
