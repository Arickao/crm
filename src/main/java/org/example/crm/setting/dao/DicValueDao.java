package org.example.crm.setting.dao;



import org.example.crm.setting.domain.DicValue;

import java.util.List;

public interface DicValueDao {

    /**
     * 根据字典类型取得相应的值
     *
     * @param code 字典类型
     * @return
     */
    List<DicValue> getListByCode(String code);
}
