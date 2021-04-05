package org.example.crm.setting.service.impl;

import org.example.crm.setting.dao.DicTypeDao;
import org.example.crm.setting.dao.DicValueDao;
import org.example.crm.setting.domain.DicType;
import org.example.crm.setting.domain.DicValue;
import org.example.crm.setting.service.DicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DicServiceImpl implements DicService {

    @Autowired
    private DicTypeDao dicTypeDao;

    @Autowired
    private DicValueDao dicValueDao;

    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String, List<DicValue>> map = new HashMap<>(16);

        List<DicType> dtList = dicTypeDao.getTypeList();

        for (DicType dt: dtList) {
            String code = dt.getCode();
            List<DicValue> dvList = dicValueDao.getListByCode(code);
            map.put(code,dvList);
        }

        return map;
    }
}
