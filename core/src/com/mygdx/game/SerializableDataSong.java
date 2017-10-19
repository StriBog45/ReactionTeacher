package com.mygdx.game;

import java.io.Serializable;

/**
 * Created by StriBog on 6/18/2017.
 */

public class SerializableDataSong implements Serializable {
    float[][] probabilityTable;
    float[] possibility;
    DataSetCircle[] dataSetCircles;
    DataSetSlider[] dataSetSliders;
}
