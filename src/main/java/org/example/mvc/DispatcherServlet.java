package org.example.mvc;

import org.example.mvc.controller.Controller;
import org.example.mvc.controller.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// HttpServlet을 상속하면 서블릿이 되고, 톰캣이 이를 실행할 수 있게 된다
@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
    private final static Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    // 서블릿의 초기화는 톰캣 실행 시
    @Override
    public void init() throws ServletException {
        requestMappingHandlerMapping = new RequestMappingHandlerMapping();
        requestMappingHandlerMapping.init();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("[DispatcherServlet] service started");
        try {
            // 요청 URI에 대한 처리가 가능한 핸들러를 달라는 메서드
            Controller handler = requestMappingHandlerMapping.findHandler(new HandlerKey(RequestMethod.valueOf(req.getMethod()), req.getRequestURI()));
            // ** URI는 스트링, URL은 스트링 버퍼

            // 컨트롤러에게 작업을 위임
            // "redirect:/users"
            // forwarding과 redirecting은 분리되어야 한다 ~ view resolver의 역할 : 리다이렉트 뷰인지, jsp 뷰인지 판단한다
            String viewName = handler.handleRequest(req, resp);

            // 요청 url를 가지고 요청 dispatcher를 가져온 뒤, 해당하는 view로 전달하는 코드
//            RequestDispatcher requestDispatcher = req.getRequestDispatcher(viewName);
//            requestDispatcher.forward(req, resp);

        } catch (Exception e) {
            logger.error("exception occurred : [{}]", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
