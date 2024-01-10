package org.example.mvc;

import org.example.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter{
    @Override
    public boolean supports(Object handler) {
        return handler instanceof Annotationhandler;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Annotation이 있는 클래스의 handler라면 viewName을 반환하는 메서드
        String viewName = ((Annotationhandler) handler).handle(request, response);
        return new ModelAndView(viewName);
    }
}
