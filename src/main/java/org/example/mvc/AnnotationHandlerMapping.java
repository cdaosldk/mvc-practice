package org.example.mvc;

import org.example.mvc.annotation.Controller;
import org.example.mvc.annotation.RequestMapping;
import org.example.mvc.controller.RequestMethod;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping{
    private final Object[] basePakage;
    private Map<HandlerKey, Annotationhandler> handlers = new HashMap<>();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePakage = basePackage;
    }

    // @Controller 클래스를 리플렉션으로 전부 다 가져온다
    public void initialize() {
        Reflections reflections = new Reflections(basePakage);

        // 실습 상 HomeContorller만 넘어온다
        Set<Class<?>> clazzsWithControllerAnnotation = reflections.getTypesAnnotatedWith(Controller.class);

        // 각 클래스에서 메서드에 붙어있는 값을 추출하기 위해서
        clazzsWithControllerAnnotation.forEach(clazz ->
                Arrays.stream(clazz.getDeclaredMethods()).forEach(declaredMethod -> {
                    RequestMapping requestMapping = declaredMethod.getDeclaredAnnotation(RequestMapping.class);

                    Arrays.stream(getRequestMethods(requestMapping))
                            .forEach(requestMethod -> handlers.put(
                                    new HandlerKey(requestMethod, requestMapping.value()), new Annotationhandler(clazz, declaredMethod)
                            ));
                }));
    }

    //
    private RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        return requestMapping.method();
    }

    @Override
    public Object findHandler(HandlerKey handlerKey) {
        return handlers.get(handlerKey);
    }
}
