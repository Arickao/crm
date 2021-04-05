package org.example.crm.workbench.web.controller;

import org.example.crm.setting.domain.User;
import org.example.crm.setting.service.UserService;
import org.example.crm.utils.DateTimeUtil;
import org.example.crm.utils.UUIDUtil;
import org.example.crm.vo.PaginationVO;
import org.example.crm.workbench.domain.Contacts;
import org.example.crm.workbench.domain.Customer;
import org.example.crm.workbench.domain.Tran;
import org.example.crm.workbench.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/customer")
public class CustomerController {

    @Resource
    private CustomerService customerService;

    @Resource
    private UserService userService;

    @RequestMapping("/pageList")
    @ResponseBody
    public PaginationVO<Customer> pageList(Integer pageNo, Integer pageSize, Customer customer){
        // 此处代码待完善，map传值为null，作条件分页查询
        Map<String,Object> map = new HashMap<>(16);

        PaginationVO<Customer> vo = customerService.pageList(pageNo,pageSize,map);
        return vo;
    }

    @RequestMapping("/detail")
    public ModelAndView detail(String id){
        Customer customer = customerService.detail(id);

        ModelAndView mv = new ModelAndView();
        mv.addObject("c",customer);
        mv.setViewName("/workbench/customer/detail.jsp");
        return mv;
    }

    @RequestMapping("/getAllUserIdAndName")
    @ResponseBody
    public List<User> getAllUserIdAndName(){
        List<User> userList = userService.getAllUserIdAndName();
        return userList;
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ModelAndView save(Customer customer, HttpServletRequest request){
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        customer.setId(UUIDUtil.getUUID());
        customer.setCreateBy(createBy);
        customer.setCreateTime(createTime);

        boolean flag = customerService.save(customer);

        ModelAndView mv = new ModelAndView();
        if (flag){
            mv.setViewName("redirect:/workbench/customer/index.jsp");
        }else{
            /// mv.setViewName("/WEB-INF/msg/tran.jsp");
        }
        return mv;
    }

}
