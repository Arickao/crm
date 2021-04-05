package org.example.crm.workbench.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.crm.exception.ActivityDeleteException;
import org.example.crm.setting.dao.UserDao;
import org.example.crm.setting.domain.User;
import org.example.crm.vo.PaginationVO;
import org.example.crm.workbench.dao.ActivityDao;
import org.example.crm.workbench.dao.ActivityRemarkDao;
import org.example.crm.workbench.domain.Activity;
import org.example.crm.workbench.domain.ActivityRemark;
import org.example.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    UserDao userDao;

    @Autowired
    ActivityDao activityDao;

    @Autowired
    ActivityRemarkDao activityRemarkDao;

    @Override
    public boolean save(Activity activity) {
        boolean tag = false;
        int num = activityDao.save(activity);
        if (num == 1){
            tag = true;
        }
        return true;
    }

    @Override
    public PaginationVO<Activity> pageList(Integer pageNo, Integer pageSize, Map<String, Object> map) {
        // 加入PageHelper的方法，分页
        Page p = PageHelper.startPage(pageNo,pageSize);
        List<Activity> activityList = activityDao.getActivityByCondition(map);
        Long total = p.getTotal();

        PaginationVO<Activity> vo = new PaginationVO<>();
        vo.setTotal(total);
        vo.setDataList(activityList);
        return vo;
    }

    @Override
    public Map getUserListAndActivity(String id) {
        Map<String,Object> map = new HashMap<>(16);
        List<User> uList = userDao.getAllUserIdAndName();
        Activity activity = activityDao.getById(id);

        map.put("uList",uList);
        map.put("a",activity);
        return map;
    }

    @Override
    public boolean update(Activity activity) {
        boolean flag = activityDao.update(activity);
        return flag;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,
            readOnly = false,rollbackFor = {ActivityDeleteException.class})
    public boolean delete(String[] ids) {
        boolean flag = false;

        // 查询出需要删除备注的数量
        int count1 = activityRemarkDao.getCountByAids(ids);

        // 删除备注，返回受到影响的条数
        int count2 = activityRemarkDao.deleteByAids(ids);
        if (count1 != count2){
            throw new ActivityDeleteException("市场活动备注查询数量与删除数量不符");
        }

        // 删除市场活动
        int count = activityDao.delete(ids);
        if (count == ids.length){
            flag = true;
        }

        return flag;
    }

    @Override
    public Activity detail(String id) {
        Activity activity = activityDao.detail(id);
        return activity;
    }

    @Override
    public boolean saveRemark(ActivityRemark activityRemark) {
        boolean flag = false;
        int nums = activityRemarkDao.save(activityRemark);
        if (nums == 1){
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark activityRemark) {
        boolean flag = false;
        int nums = activityRemarkDao.update(activityRemark);
        if (nums == 1){
            flag = true;
        }
        return false;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        List<ActivityRemark> arList = activityRemarkDao.getRemarkListByAid(activityId);
        // 对返回的数据进行重排序
        arList.sort(new Comparator<ActivityRemark>() {
            @Override
            public int compare(ActivityRemark a1, ActivityRemark a2) {
                String t1 = "0".equals(a1.getEditFlag()) ? a1.getCreateTime() : a1.getEditTime();
                String t2 = "0".equals(a2.getEditFlag()) ? a2.getCreateTime() : a2.getEditTime();

                return t1.compareTo(t2);
            }
        });
        return arList;
    }

    @Override
    public boolean deleteActivityRemark(String id) {
        boolean flag = false;
        int nums = activityRemarkDao.deleteById(id);
        if (nums == 1){
            flag = true;
        }
        return flag;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {
        List<Activity> aList = activityDao.getActivityListByClueId(clueId);
        return aList;
    }

    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(String aname, String clueId) {
        Map<String,Object> map = new HashMap<>(16);
        map.put("aname",aname);
        map.put("clueId",clueId);
        List<Activity> aList = activityDao.getActivityListByNameAndNotByClueId(map);
        return aList;
    }

    @Override
    public List<Activity> getActivityLikeName(String name) {
        Map<String,Object> map = new HashMap<>(16);
        map.put("name",name);
        List<Activity> aList = activityDao.getActivityByCondition(map);
        return aList;
    }
}
