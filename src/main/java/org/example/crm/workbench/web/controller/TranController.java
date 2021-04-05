package org.example.crm.workbench.web.controller;

import org.example.crm.setting.domain.User;
import org.example.crm.setting.service.UserService;
import org.example.crm.utils.DateTimeUtil;
import org.example.crm.utils.UUIDUtil;
import org.example.crm.vo.PaginationVO;
import org.example.crm.workbench.domain.Tran;
import org.example.crm.workbench.domain.TranHistory;
import org.example.crm.workbench.service.CustomerService;
import org.example.crm.workbench.service.TranService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/transaction")
public class TranController {

    @Resource
    private UserService userService;

    @Resource
    private TranService tranService;

    @Resource
    private CustomerService customerService;

    @RequestMapping("/add")
    @ResponseBody
    public ModelAndView add(){
        ModelAndView mv = new ModelAndView();
        List<User> userList = userService.getAllUserIdAndName();
        mv.addObject("uList",userList);
        mv.setViewName("/workbench/transaction/save.jsp");
        return mv;
    }

    @RequestMapping("/getCustomerName")
    @ResponseBody
    public List<String> getCustomerName(String name){
        List<String> sList = customerService.getCustomerName(name);
        return sList;
    }

    @RequestMapping(value = "/save")
    public ModelAndView save(Tran tran, HttpServletRequest request,String customerName){
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        tran.setId(UUIDUtil.getUUID());
        tran.setCreateBy(createBy);
        tran.setCreateTime(createTime);

        boolean flag = tranService.save(tran,customerName);

        ModelAndView mv = new ModelAndView();
        if (flag){
            mv.setViewName("redirect:/workbench/transaction/index.jsp");
        }else{
            mv.setViewName("/WEB-INF/msg/tran.jsp");
        }
        return mv;
    }

    @RequestMapping("/pageList")
    @ResponseBody
    public PaginationVO<Tran> pageList(Integer pageNo, Integer pageSize, Tran tran){
        // 此处代码待完善，map传值为null，作条件分页查询
        Map<String,Object> map = new HashMap<>(16);

        PaginationVO<Tran> vo = tranService.pageList(pageNo,pageSize,map);
        return vo;
    }

    @RequestMapping("/detail")
    public ModelAndView detail(String id, HttpServletRequest request){
        Tran tran = tranService.detail(id);

        ModelAndView mv = new ModelAndView();
        String stage = tran.getStage();

        ServletContext application = request.getServletContext();
        Map<String, String> map = (Map<String, String>) application.getAttribute("pMap");
        String possibility = map.get(stage);

        mv.addObject("t",tran);
        mv.addObject("possibility",possibility);
        mv.setViewName("/workbench/transaction/detail.jsp");
        return mv;
    }

    @RequestMapping("/getHistoryListByTranId")
    @ResponseBody
    public List<TranHistory> getHistoryListByTranId(String tranId,HttpServletRequest request){
        ServletContext application = request.getServletContext();
        Map<String, String> map = (Map<String, String>) application.getAttribute("pMap");

        List<TranHistory> thList = tranService.getHistoryListByTranId(tranId);
        for (TranHistory th : thList){
            String stage = th.getStage();
            String possibility = map.get(stage);
            th.setPossibility(possibility);
        }

        return thList;
    }

    @RequestMapping(value = "/changeStage",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> changeStage(Tran tran,HttpServletRequest request){
        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();

        tran.setEditTime(editTime);
        tran.setEditBy(editBy);

        boolean flag = tranService.changeStage(tran);

        ServletContext application = request.getServletContext();
        Map<String, String> pMap = (Map<String, String>) application.getAttribute("pMap");
        String possibility = pMap.get(tran.getStage());
        tran.setPossibility(possibility);

        Map<String, Object> map = new HashMap<>(16);
        map.put("success",flag);
        map.put("tran",tran);

        return map;
    }

    @RequestMapping("/getCharts")
    @ResponseBody
    public Map<String,Object> getCharts(){
        Map<String,Object> map = tranService.getCharts();

        return map;
    }
}
