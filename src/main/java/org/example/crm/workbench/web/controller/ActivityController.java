package org.example.crm.workbench.web.controller;

import org.example.crm.setting.domain.User;
import org.example.crm.setting.service.UserService;
import org.example.crm.utils.DateTimeUtil;
import org.example.crm.utils.UUIDUtil;
import org.example.crm.vo.PaginationVO;
import org.example.crm.workbench.domain.Activity;
import org.example.crm.workbench.domain.ActivityRemark;
import org.example.crm.workbench.service.ActivityService;
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
@RequestMapping("/workbench/activity")
public class ActivityController {

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

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public boolean saveActivity(Activity activity, HttpServletRequest request){
        //创建人，当前登录用户
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String creatTime = DateTimeUtil.getSysTime();
        activity.setId(UUIDUtil.getUUID());
        activity.setCreateTime(creatTime);
        activity.setCreateBy(createBy);

        boolean success = activityService.save(activity);
        return success;
    }

    @RequestMapping("/pageList")
    @ResponseBody
    public PaginationVO<Activity> pageList(Integer pageNo, Integer pageSize, Activity activity){
        // 分页查询结果
        Map<String,Object> map = new HashMap<>(16);
        map.put("name",activity.getName());
        map.put("owner",activity.getOwner());
        map.put("startDate",activity.getStartDate());
        map.put("endDate",activity.getEndDate());

        PaginationVO<Activity> vo = activityService.pageList(pageNo,pageSize,map);
        return vo;

    }

    @RequestMapping("/getUserListAndActivity")
    @ResponseBody
    public Map<String,Object> getUserListAndActivity(String id){
        // 市场活动修改操作
        Map map = activityService.getUserListAndActivity(id);
        return map;
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public boolean update(HttpServletRequest request,Activity activity){
        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();
        activity.setEditBy(editBy);
        activity.setEditTime(editTime);
        boolean flag = activityService.update(activity);
        return flag;
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public boolean delete(HttpServletRequest request){
        String[] ids = request.getParameterValues("id");
        boolean flag = activityService.delete(ids);
        return flag;
    }

    @RequestMapping("/detail")
    public ModelAndView detail(String id){
        ModelAndView mv = new ModelAndView();
        Activity activity = activityService.detail(id);
        mv.addObject("a",activity);
        mv.setViewName("/workbench/activity/detail.jsp");
        return mv;
    }

    @RequestMapping(value = "/saveRemark",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> saveRemark(ActivityRemark activityRemark,HttpServletRequest request){
        String createBy = ((User) request.getSession().getAttribute("user")).getName();

        activityRemark.setId(UUIDUtil.getUUID());
        activityRemark.setCreateBy(createBy);
        activityRemark.setCreateTime(DateTimeUtil.getSysTime());
        activityRemark.setEditFlag("0");

        boolean flag = activityService.saveRemark(activityRemark);

        Map<String,Object> map = new HashMap<>(16);
        map.put("success",flag);
        map.put("ar",activityRemark);
        return map;
    }

    @RequestMapping(value = "/updateRemark",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateRemark(ActivityRemark activityRemark,HttpServletRequest request){
        String editBy = ((User) request.getSession().getAttribute("user")).getName();

        activityRemark.setEditFlag("1");
        activityRemark.setEditBy(editBy);
        activityRemark.setEditTime(DateTimeUtil.getSysTime());

        boolean flag = activityService.updateRemark(activityRemark);
        Map<String,Object> map = new HashMap<>(16);
        map.put("success",flag);
        map.put("ar",activityRemark);

        return map;
    }

    @RequestMapping("/getRemarkListByAid")
    @ResponseBody
    public List<ActivityRemark> getRemarkListByAid(String activityId){
        List<ActivityRemark> arList = activityService.getRemarkListByAid(activityId);
        return arList;
    }

    @RequestMapping(value = "/deleteRemark",method = RequestMethod.POST)
    @ResponseBody
    public boolean deleteActivityRemark(String id){
        boolean flag = activityService.deleteActivityRemark(id);
        return flag;
    }

    @RequestMapping("/getActivityListByClueId")
    @ResponseBody
    public List<Activity> getActivityListByClueId(String clueId){
        List<Activity> aList = activityService.getActivityListByClueId(clueId);
        return aList;
    }


    @RequestMapping("/getActivityLikeName")
    @ResponseBody
    public List<Activity> getActivityLikeName(String name){
        List<Activity> aList = activityService.getActivityLikeName(name);
        return aList;
    }


}
