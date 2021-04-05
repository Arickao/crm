package org.example.crm.workbench.dao;

import org.example.crm.workbench.domain.Customer;
import org.example.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer customer);

    List<String> getNameList(String name);

    List<Customer> getCustomerByCondition(Map<String, Object> map);

    Customer getCustomerById(String id);
}
