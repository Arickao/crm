package org.example.crm.workbench.dao;

import org.example.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    /**
     *
     * 添加市场活动
     *
     * @param activity
     * @return
     */
    int save(Activity activity);

    /**
     *
     * 根据条件查询查询
     *
     * @param map
     * @return Activity
     */
    List<Activity> getActivityByCondition(Map<String, Object> map);

    /**
     *
     * 根据id查Activity单条返回基础信息
     *
     * @param id
     * @return Activity
     */
    Activity getById(String id);


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
    int delete(String[] ids);

    /**
     *
     * 根据id查单条全部信息
     *
     * @param id
     * @return
     */
    Activity detail(String id);

    /**
     *
     * @param clueId
     * @return
     */
    List<Activity> getActivityListByClueId(String clueId);

    /**
     *
     * @param map
     * @return
     */
    List<Activity> getActivityListByNameAndNotByClueId(Map<String, Object> map);


}
