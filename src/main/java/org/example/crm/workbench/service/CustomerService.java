package org.example.crm.workbench.service;

import org.example.crm.vo.PaginationVO;
import org.example.crm.workbench.domain.Contacts;
import org.example.crm.workbench.domain.Customer;
import org.example.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    List<String> getCustomerName(String name);

    PaginationVO<Customer> pageList(Integer pageNo, Integer pageSize, Map<String, Object> map);

    Customer detail(String id);

    boolean save(Customer customer);
}
