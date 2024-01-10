package org.example.mvc;

import org.example.mvc.annotation.RequestMapping;
import org.example.mvc.controller.RequestMethod;
import org.example.mvc.view.JspViewResolver;
import org.example.mvc.view.ModelAndView;
import org.example.mvc.view.View;
import org.example.mvc.view.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

// HttpServlet을 상속하면 서블릿이 되고, 톰캣이 이를 실행할 수 있게 된다
@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
    private final static Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappingList;

    private List<HandlerAdapter> handlerAdapterList;

    private List<ViewResolver> viewResolverList;

    // 서블릿의 초기화는 톰캣 실행 시
    @Override
    public void init() throws ServletException {
        RequestMappingHandlerMapping rmhm = new RequestMappingHandlerMapping();
        rmhm.init();

        // 어노테이션 요청(@Controller)도 처리하기 위함
        AnnotationHandlerMapping ahm = new AnnotationHandlerMapping("org.example");

        handlerMappingList = List.of(rmhm, ahm);
        handlerAdapterList = List.of(new SimpleControllerHandlerAdapter(), new AnnotationHandlerAdapter());
        viewResolverList = Collections.singletonList(new JspViewResolver());
    }

    // 1. 요청이 들어오면 rmhm이 handler를 찾는다
    // 2. handlerAdapterList에서 handler가 컨트롤러를 구현하는 지 확인 후 핸들러 어댑터로 반환한다
    // 3. 핸들러 어댑터에서 handle 메서드로 ModelAndView 반환
    // 4. viewName으로 ViewResolver에서 view 반환 후 view.render메서도로 화면 구현
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("[DispatcherServlet] service started");
        String requestURI = req.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(req.getMethod());

        try {
            // 요청 URI에 대한 처리가 가능한 핸들러를 달라는 메서드
//            Controller handler = handlerMapping.findHandler(new HandlerKey(RequestMethod.valueOf(req.getMethod()), req.getRequestURI()));
            Object handler = handlerMappingList.stream()
                            .filter(hm -> hm.findHandler(new HandlerKey(requestMethod, requestURI)) != null)
                            .map(hm -> hm.findHandler(new HandlerKey(requestMethod, requestURI)))
                            .findFirst()
                            .orElseThrow(() -> new ServletException("No Handler for [ " + requestMethod + ", " + requestURI + "]"));
//            findHandler(new HandlerKey(RequestMethod.valueOf(req.getMethod()), req.getRequestURI()));
            // ** URI는 스트링, URL은 스트링 버퍼

            // 컨트롤러에게 작업을 위임
            // "redirect:/users"
            // forwarding과 redirecting은 분리되어야 한다 ~ view resolver의 역할 : 리다이렉트 뷰인지, jsp 뷰인지 판단한다
//            String viewName = handler.handleRequest(req, resp);

            // 요청 url를 가지고 요청 dispatcher를 가져온 뒤, 해당하는 view로 전달하는 코드
//            RequestDispatcher requestDispatcher = req.getRequestDispatcher(viewName);
//            requestDispatcher.forward(req, resp);

            HandlerAdapter handlerAdapter = handlerAdapterList.stream()
                    .filter(ha -> ha.supports(handler))
                    .findFirst()
                    .orElseThrow(() -> new ServletException("No adapter for [" + handler + "]"));

            ModelAndView modelAndView = handlerAdapter.handle(req, resp, handler);

            for (ViewResolver viewResolver : viewResolverList) {
                View view = viewResolver.resolveView(modelAndView.getViewName());
//                view.render(new HashMap<>(), req, resp);
                view.render(modelAndView.getModel(), req, resp);
            }

        } catch (Exception e) {
            logger.error("exception occurred : [{}]", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
