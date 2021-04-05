package org.example.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @RequestMapping
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:/welcome/index.html");
        return mv;
    }

}
