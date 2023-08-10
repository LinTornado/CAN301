package com.example.pair_click.component;

public class Dot {
    public static  int STATE_NORMAL=0;
    public static  int STATE_PRESS=1;
    public static  int STATE_ERROR=2;
    public float x,y;
    public int state = 0;

    public Dot(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean inornot(float placex, float placey, float radius){
        float gap = (float) Math.sqrt((x - placex) * (x - placex) + (y - placey) * (y - placey));
        return gap <= radius;
    }
}
