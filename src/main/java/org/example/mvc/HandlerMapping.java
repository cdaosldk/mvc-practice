package org.example.mvc;

import org.example.mvc.controller.Controller;

public interface HandlerMapping {

    // 어노테이션도 받을 수 있도록 Obejct 사용
    Object findHandler(HandlerKey handlerKey);
}
