package com.demo.generator;

public interface Counter<T>{

    T getNextCount();

    void reset();
}
