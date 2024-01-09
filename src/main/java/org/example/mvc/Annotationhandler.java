package org.example.mvc;

import java.lang.reflect.Method;

public class Annotationhandler {
    private final Method targetMethod;
    private final Class<?> clazz;

    public Annotationhandler(Class<?> clazz, Method targetMethod) {
        this.clazz = clazz;
        this.targetMethod = targetMethod;
    }
}
