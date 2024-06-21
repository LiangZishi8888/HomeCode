package com.generator;

public interface Counter<T>{

    T getNextCount();

    void reset();
}
