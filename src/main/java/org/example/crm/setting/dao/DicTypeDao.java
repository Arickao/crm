package org.example.crm.setting.dao;



import org.example.crm.setting.domain.DicType;

import java.util.List;

public interface DicTypeDao {

    /**
     * 获取字典数据类型
     *
     * @return
     */
    List<DicType> getTypeList();
}
