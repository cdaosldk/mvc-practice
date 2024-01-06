package org.example.mvc;

import org.example.mvc.controller.*;

import java.util.HashMap;
import java.util.Map;

public class RequestMappingHandlerMapping {

    // url path와 controller를 매핑하는 역할
    private Map<HandlerKey, Controller> mappings = new HashMap<>();

    void init() {
        mappings.put(new HandlerKey(RequestMethod.GET, "/"), new HomeController());
        mappings.put(new HandlerKey(RequestMethod.GET, "/users"), new UserListController());
        // form.jsp에서 /users로 post 요청을 보내면 dispatcherServlet이 받고, 이후 RMHM에서 알맞은 컨트롤러로 연결한다
        mappings.put(new HandlerKey(RequestMethod.POST, "/users"), new UserCreateController());
        // forward는 요청이 들어올 경우 해당 url로 이동시킴
        mappings.put(new HandlerKey(RequestMethod.GET, "/user/form"), new ForwardController("/user/form.jsp"));
    }

    public Controller findHandler(HandlerKey handlerKey) {
        return mappings.get(handlerKey);
    }
}
