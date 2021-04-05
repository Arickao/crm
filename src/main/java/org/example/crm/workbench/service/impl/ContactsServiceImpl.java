package org.example.crm.workbench.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.crm.exception.CreateTranException;
import org.example.crm.utils.UUIDUtil;
import org.example.crm.vo.PaginationVO;
import org.example.crm.workbench.dao.ContactsDao;
import org.example.crm.workbench.dao.CustomerDao;
import org.example.crm.workbench.domain.Contacts;
import org.example.crm.workbench.domain.Customer;
import org.example.crm.workbench.domain.Tran;
import org.example.crm.workbench.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ContactsServiceImpl implements ContactsService {

    @Autowired
    private ContactsDao contactsDao;

    @Autowired
    private CustomerDao customerDao;

    @Override
    public List<Contacts> getContactsLikeName(String fullname) {
        List<Contacts> cList = contactsDao.getContactsLikeName(fullname);
        return cList;
    }

    @Override
    public PaginationVO<Contacts> pageList(Integer pageNo, Integer pageSize, Map<String, Object> map) {
        Page p = PageHelper.startPage(pageNo,pageSize);
        List<Contacts> customerList = contactsDao.getContactsByMap(map);
        Long total = p.getTotal();

        PaginationVO<Contacts> vo = new PaginationVO<>();
        vo.setTotal(total);
        vo.setDataList(customerList);
        return vo;

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,
            readOnly = false,rollbackFor = {CreateTranException.class})
    public boolean save(Contacts contacts, String customerName) {
        Customer customer = retCustomer(customerName,contacts);
        String customerId = customer.getId();

        contacts.setCustomerId(customerId);
        int nums = contactsDao.save(contacts);
        if (nums != 1){
            throw new CreateTranException("创建联系人失败");
        }

        return true;

    }

    @Override
    public Contacts detail(String id) {
        Contacts contacts = contactsDao.getContactsById(id);
        return contacts;
    }

    private Customer retCustomer(String customerName, Contacts contacts) {
        Customer customer = customerDao.getCustomerByName(customerName);
        if (customer == null){
            customer = new Customer();

            customer.setId(UUIDUtil.getUUID());
            customer.setOwner(contacts.getOwner());
            customer.setName(customerName);
            customer.setCreateBy(contacts.getCreateBy());
            customer.setCreateTime(contacts.getCreateTime());
            customer.setContactSummary(contacts.getContactSummary());
            customer.setNextContactTime(contacts.getNextContactTime());
            customer.setDescription(contacts.getDescription());

            int nums = customerDao.save(customer);
            if (nums != 1){
                throw new CreateTranException("创建客户失败");
            }

        }
        return customer;
    }
}
