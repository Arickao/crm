package org.example.crm.workbench.dao;

import org.example.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran tran);

    /**
     *
     * 备注：此代码根据条件分页查询，现阶段主要实现分页查询，条件查询后续实现
     *
     * @param map
     * @return
     */
    List<Tran> getTranByCondition(Map<String, Object> map);

    Tran detail(String id);

    int changeStage(Tran tran);

    int getTotal();

    List<Map<String, Object>> getCharts();
}
