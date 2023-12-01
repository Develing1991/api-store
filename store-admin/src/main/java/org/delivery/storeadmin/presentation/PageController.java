package org.delivery.storeadmin.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("")
public class PageController {
    @RequestMapping(path = { "", "/main" }, method = RequestMethod.GET)
    public ModelAndView main(){
        return new ModelAndView("main"); // main.html로 매핑해 줌
    }

    @RequestMapping(path = "/order", method = RequestMethod.GET )
    public ModelAndView order(){
        return new ModelAndView("order/order");
    }
}
