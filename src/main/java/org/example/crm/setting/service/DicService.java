package org.example.crm.setting.service;

import org.example.crm.setting.domain.DicValue;

import java.util.List;
import java.util.Map;

public interface DicService {

    /**
     *
     * 取得字典类型与字典值，保存在map集合
     *
     *
     * @return String字典类型，List该类型的值
     */
    Map<String, List<DicValue>> getAll();

}
