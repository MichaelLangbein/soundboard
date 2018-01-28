package org.langbein.michael.soundboard.scenes.utils;



public class Vec2<T> {

    public T x;
    public T y;

    public Vec2(T x, T y){
        this.x = x;
        this.y = y;
    }

//    public float distance(Vec2<T> other) {
//        return Math.sqrt( Math.pow(x -  other.x, 2) + Math.pow(y - other.y, 2) );
//    }
}
