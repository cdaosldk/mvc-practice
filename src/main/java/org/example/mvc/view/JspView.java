package org.example.mvc.view;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class JspView implements View{
    private final String viewName;

    public JspView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // request.setAttribute("users", List.of());를 foreach하는 클린코드
        model.forEach(request::setAttribute);

        // forward 방식
        // 요청 url를 가지고 요청 dispatcher를 가져온 뒤, 해당하는 view로 전달하는 코드
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
