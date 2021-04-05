package org.example.crm.workbench.service;

import org.example.crm.vo.PaginationVO;
import org.example.crm.workbench.domain.Contacts;

import java.util.List;
import java.util.Map;

public interface ContactsService {

    List<Contacts> getContactsLikeName(String fullname);

    PaginationVO<Contacts> pageList(Integer pageNo, Integer pageSize, Map<String, Object> map);

    boolean save(Contacts contacts, String customerName);

    Contacts detail(String id);
}
