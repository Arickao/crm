package org.example.crm.workbench.service;

import org.example.crm.vo.PaginationVO;
import org.example.crm.workbench.domain.Activity;
import org.example.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    /**
     *
     * @param activity 添加市场活动
     * @return 添加成功 失败
     */
    boolean save(Activity activity);


    /**
     *
     * Activity 的分页查询
     *
     * @param pageNo 第几页
     * @param pageSize 单页条数
     * @param map 分页查询的条件：name,owner,startDate,endDate
     * @return 一个Activity的List集合 和一个total
     */
    PaginationVO<Activity> pageList(Integer pageNo,Integer pageSize,Map<String, Object> map);

    /**
     *
     *
     *
     * @param id d根据id查询市场活动所有信息
     * @return map uList：用户id和name ，activity
     */
    Map<String,Object> getUserListAndActivity(String id);

    /**
     *
     * 更新activity
     *
     * @param activity
     * @return
     */
    boolean update(Activity activity);



    /**
     *
     * 根据传入的id数组信息从表中删除数据
     *
     * @param ids
     * @return
     */
    boolean delete(String[] ids);

    /**
     * 根据id查单条全部信息
     *
     * @param id
     * @return
     */
    Activity detail(String id);

    /**
     * 市场活动备注保存
     *
     * @param activityRemark
     * @return
     */
    boolean saveRemark(ActivityRemark activityRemark);

    /**
     *
     * 通过id修改市场活动备注信息
     *
     * @param activityRemark
     * @return
     */
    boolean updateRemark(ActivityRemark activityRemark);

    /**
     *
     * 根据市场活动id取得备注信息列表
     *
     * @param activityId
     * @return
     */
    List<ActivityRemark> getRemarkListByAid(String activityId);

    /**
     *
     * 根据id删除市场活动备注
     *
     * @param id
     * @return
     */
    boolean deleteActivityRemark(String id);

    /**
     * 通过线序的id在线索与市场关系表中查找市场活动id，返回市场活动信息，返回的Activity id为线索与市场活动关系表中的id
     *
     * @param clueId
     * @return
     */
    List<Activity> getActivityListByClueId(String clueId);

    /**
     *
     * 根据市场活动名称模糊查找市场活动，去除 在关系表中找到已关联的市场活动
     * @param aname
     * @param clueId
     * @return
     */
    List<Activity> getActivityListByNameAndNotByClueId(String aname, String clueId);

    List<Activity> getActivityLikeName(String name);
}
