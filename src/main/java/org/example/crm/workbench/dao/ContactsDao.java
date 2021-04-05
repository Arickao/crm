package org.example.crm.workbench.dao;

import org.example.crm.workbench.domain.Contacts;

import java.util.List;
import java.util.Map;

public interface ContactsDao {

    /**
     * 根据fullname与customerId查找Contacts
     * @param map
     * @return
     */
    Contacts getContactsByCondition(Map<String, Object> map);


    /**
     * 添加客户信息
     * @param contacts
     * @return
     */
    int save(Contacts contacts);


    List<Contacts> getContactsLikeName(String fullname);

    List<Contacts> getContactsByMap(Map<String, Object> map);

    Contacts getContactsById(String id);
}
