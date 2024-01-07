package org.example.mvc.view;

// View Name을 받아서 View를 결정하는 역할
public interface ViewResolver {
    View resolveView(String viewName);
}
