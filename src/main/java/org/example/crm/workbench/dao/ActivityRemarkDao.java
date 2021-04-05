package org.example.crm.workbench.dao;

import org.example.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {

    /**
     * 保存市场活动
     *
     * @param activityRemark
     * @return
     */
    int save(ActivityRemark activityRemark);


    /**
     * 通过id修改市场活动备注信息
     *
     * @param activityRemark
     * @return
     */
    int update(ActivityRemark activityRemark);

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
     *
     */
    int deleteById(String id);

    /**
     *
     * 根据传入的id数组查找市场活动备注中相关条数
     *
     * @param ids
     * @return
     */
    int getCountByAids(String[] ids);

    /**
     *
     * 根据传入的id数组删除市场活动备注
     *
     * @param ids
     * @return
     */
    int deleteByAids(String[] ids);
}
