package org.example.mvc.controller;

import org.example.mvc.model.User;
import org.example.mvc.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserCreateController implements Controller{
    @Override
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserRepository.save(new User(request.getParameter("userId"), request.getParameter("name")));
        // 레포지토리에 유저를 저장하고 난 뒤 /users를 다시 요청한다 ~ /users로 get 요청시, uri와 머소드가 일치하는 컨트롤러가 있기 떄문에 해당 컨트롤러를 매핑
        return "redirect:/users";
    }
}
