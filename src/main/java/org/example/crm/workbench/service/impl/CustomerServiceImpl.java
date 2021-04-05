package org.example.crm.workbench.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.crm.vo.PaginationVO;
import org.example.crm.workbench.dao.CustomerDao;
import org.example.crm.workbench.domain.Contacts;
import org.example.crm.workbench.domain.Customer;
import org.example.crm.workbench.domain.Tran;
import org.example.crm.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Override
    public List<String> getCustomerName(String name) {
        List<String> sList = customerDao.getNameList(name);
        return sList;
    }

    @Override
    public PaginationVO<Customer> pageList(Integer pageNo, Integer pageSize, Map<String, Object> map) {
        Page p = PageHelper.startPage(pageNo,pageSize);
        List<Customer> customerList = customerDao.getCustomerByCondition(map);
        Long total = p.getTotal();

        PaginationVO<Customer> vo = new PaginationVO<>();
        vo.setTotal(total);
        vo.setDataList(customerList);
        return vo;
    }

    @Override
    public Customer detail(String id) {
        Customer customer = customerDao.getCustomerById(id);
        return customer;
    }

    @Override
    public boolean save(Customer customer) {
        boolean flag = false;
        int count = customerDao.save(customer);
        if (count == 1) {
            flag = true;
        }
        return flag;
    }

}
