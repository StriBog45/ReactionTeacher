package com.mygdx.game;

import java.io.Serializable;

/**
 * Created by StriBog on 4/10/2017.
 */

public class DataSetCircle implements Serializable {
    DataSetCircle(int []X, int []Y, int R[], int []Time, float []Speed){
        x = X;
        y = Y;
        r = R;
        time = Time;
        speed = Speed;
    }
    int x[];
    int y[];
    int r[];
    int time[];
    float speed[];
}
