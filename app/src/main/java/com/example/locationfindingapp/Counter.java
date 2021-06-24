package com.example.locationfindingapp;

import java.util.ArrayList;
import java.util.List;

public class Counter {
    List<Integer> counter = new ArrayList<>();

    public int getCounter(int index) {
        return counter.get(index);
    }

    public void setCounter(int counter) {
        this.counter.add(counter);
    }
    public void updateCounter(int index,int counter)
    {
        this.counter.set(index,counter);
    }
}
