package org.example.crm.workbench.web.controller;

import org.example.crm.setting.domain.User;
import org.example.crm.setting.service.UserService;
import org.example.crm.utils.DateTimeUtil;
import org.example.crm.utils.UUIDUtil;
import org.example.crm.vo.PaginationVO;
import org.example.crm.workbench.domain.*;
import org.example.crm.workbench.service.ActivityService;
import org.example.crm.workbench.service.ClueService;
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
@RequestMapping("/workbench/clue")
public class ClueController {

    @Resource
    private ClueService clueService;

    @Resource
    private UserService userService;

    @Resource
    private ActivityService activityService;

    @RequestMapping("/getAllUserIdAndName")
    @ResponseBody
    public List<User> getAllUserIdAndName(){
        List<User> userList = userService.getAllUserIdAndName();
        return userList;
    }

    @RequestMapping(value = "/saveClue",method = RequestMethod.POST)
    @ResponseBody
    public boolean saveClue(Clue clue, HttpServletRequest request){
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        clue.setId(UUIDUtil.getUUID());
        clue.setCreateTime(DateTimeUtil.getSysTime());
        clue.setCreateBy(createBy);


        boolean flag = clueService.save(clue);
        return flag;
    }

    @RequestMapping("/pageList")
    @ResponseBody
    public PaginationVO<Clue> pageList(Integer pageNo, Integer pageSize, Clue clue){
        Map<String,Object> map = new HashMap<>(16);
        PaginationVO<Clue> vo = clueService.pageList(pageNo,pageSize,map);
        return vo;
    }

    @RequestMapping("/detail")
    public ModelAndView detail(String id){
        ModelAndView mv = new ModelAndView();
        Clue clue = clueService.detail(id);
        mv.addObject("c",clue);
        mv.setViewName("/workbench/clue/detail.jsp");
        return mv;
    }

    @RequestMapping(value = "/unbind",method = RequestMethod.POST)
    @ResponseBody
    public boolean unbind(String id){
        boolean flag = clueService.unbind(id);
        return flag;
    }

    @RequestMapping("/getActivityListByNameAndNotByClueId")
    @ResponseBody
    public List<Activity> getActivityListByNameAndNotByClueId(String aname,String clueId){
        List<Activity> aList = activityService.getActivityListByNameAndNotByClueId(aname,clueId);
        return aList;
    }

    @RequestMapping(value = "/bind",method = RequestMethod.POST)
    @ResponseBody
    public boolean bind(HttpServletRequest request,String cid){
        String[] aids = request.getParameterValues("aid");
        boolean flag = clueService.bind(aids,cid);
        return flag;
    }

    @RequestMapping("/convertDetail")
    public ModelAndView convertDetail(String id){
        Clue clue = clueService.detail(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("c",clue);
        mv.setViewName("/workbench/clue/convert.jsp");
        return mv;
    }

    @RequestMapping("/convert")
    public ModelAndView convert(String clueId, Tran tran,String flag,HttpServletRequest request){
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        ModelAndView mv = new ModelAndView();

        if ("a".equals(flag)){
            tran.setId(UUIDUtil.getUUID());
            tran.setCreateTime(DateTimeUtil.getSysTime());
            tran.setCreateBy(createBy);
        }else{
            tran = null;
        }
        //boolean tag = clueService.convert(clueId,tran,createBy);
        boolean tag = false;
        if (tag){
            mv.setViewName("redirect:/workbench/clue/index.jsp");
        }else{
            mv.setViewName("/WEB-INF/msg/convert.jsp");
        }

        return mv;
    }

    @RequestMapping(value = "/saveRemark",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> saveRemark(ClueRemark clueRemark, HttpServletRequest request){
        String createBy = ((User) request.getSession().getAttribute("user")).getName();

        clueRemark.setId(UUIDUtil.getUUID());
        clueRemark.setCreateBy(createBy);
        clueRemark.setCreateTime(DateTimeUtil.getSysTime());
        clueRemark.setEditFlag("0");

        boolean flag = clueService.saveRemark(clueRemark);

        Map<String,Object> map = new HashMap<>(16);
        map.put("success",flag);
        map.put("ar",clueRemark);
        return map;
    }

    @RequestMapping("/getRemarkListByCid")
    @ResponseBody
    public List<ClueRemark> getRemarkListByCid(String clueId){
        List<ClueRemark> arList = clueService.getRemarkListByCid(clueId);
        return arList;
    }

    @RequestMapping(value = "/updateRemark",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateRemark(ClueRemark clueRemark,HttpServletRequest request){
        String editBy = ((User) request.getSession().getAttribute("user")).getName();

        clueRemark.setEditFlag("1");
        clueRemark.setEditBy(editBy);
        clueRemark.setEditTime(DateTimeUtil.getSysTime());

        boolean flag = clueService.updateRemark(clueRemark);
        Map<String,Object> map = new HashMap<>(16);
        map.put("success",flag);
        map.put("ar",clueRemark);

        return map;
    }

    @RequestMapping(value = "/deleteRemark",method = RequestMethod.POST)
    @ResponseBody
    public boolean deleteActivityRemark(String id){
        boolean flag = clueService.deleteClueRemark(id);
        return flag;
    }

}
