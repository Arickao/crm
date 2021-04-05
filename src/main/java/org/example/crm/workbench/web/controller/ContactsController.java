package org.example.crm.workbench.web.controller;

import org.example.crm.setting.domain.User;
import org.example.crm.setting.service.UserService;
import org.example.crm.utils.DateTimeUtil;
import org.example.crm.utils.UUIDUtil;
import org.example.crm.vo.PaginationVO;
import org.example.crm.workbench.domain.Contacts;
import org.example.crm.workbench.service.ContactsService;
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
@RequestMapping("/workbench/contacts")
public class ContactsController {

    @Resource
    private ContactsService contactsService;

    @Resource
    private UserService userService;

    @Resource
    private CustomerService customerService;

    @RequestMapping("/getContactsLikeName")
    @ResponseBody
    public List<Contacts> getContactsLikeName(String fullname){
        List<Contacts> cList = contactsService.getContactsLikeName(fullname);
        return cList;
    }

    @RequestMapping("/pageList")
    @ResponseBody
    public PaginationVO<Contacts> pageList(Integer pageNo, Integer pageSize, Contacts contacts){
        // 此处代码待完善，map传值为null，作条件分页查询
        Map<String,Object> map = new HashMap<>(16);

        PaginationVO<Contacts> vo = contactsService.pageList(pageNo,pageSize,map);
        return vo;
    }

    @RequestMapping("/getAllUserIdAndName")
    @ResponseBody
    public List<User> getAllUserIdAndName(){
        List<User> userList = userService.getAllUserIdAndName();
        return userList;
    }

    @RequestMapping("/getCustomerName")
    @ResponseBody
    public List<String> getCustomerName(String name){
        List<String> sList = customerService.getCustomerName(name);
        return sList;
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ModelAndView save(Contacts contacts, HttpServletRequest request, String customerName){
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(createTime);

        boolean flag = contactsService.save(contacts,customerName);

        ModelAndView mv = new ModelAndView();
        if (flag){
            mv.setViewName("redirect:/workbench/contacts/index.jsp");
        }else{
            /// mv.setViewName("/WEB-INF/msg/tran.jsp");
        }
        return mv;
    }

    @RequestMapping("/detail")
    public ModelAndView detail(String id){
        Contacts contacts = contactsService.detail(id);

        ModelAndView mv = new ModelAndView();
        mv.addObject("c",contacts);
        mv.setViewName("/workbench/contacts/detail.jsp");
        return mv;
    }
}
