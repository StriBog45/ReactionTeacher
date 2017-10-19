package com.mygdx.game;

import java.io.Serializable;

/**
 * Created by StriBog on 4/10/2017.
 */

public class DataSetSlider implements Serializable {
    DataSetSlider(int []X, int []Y, int R[], int []Time, float []Speed, int []X2, int []Y2){
        x = X;
        y = Y;
        r = R;
        time = Time;
        speed = Speed;
        x2 = X2;
        y2 = Y2;
    }
    int x[];
    int y[];
    int r[];
    int x2[];
    int y2[];
    int time[];
    float speed[];
}
