package org.example.crm.workbench.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.crm.exception.ActivityDeleteException;
import org.example.crm.exception.CreateTranException;
import org.example.crm.utils.UUIDUtil;
import org.example.crm.vo.PaginationVO;
import org.example.crm.workbench.dao.CustomerDao;
import org.example.crm.workbench.dao.TranDao;
import org.example.crm.workbench.dao.TranHistoryDao;
import org.example.crm.workbench.domain.Activity;
import org.example.crm.workbench.domain.Customer;
import org.example.crm.workbench.domain.Tran;
import org.example.crm.workbench.domain.TranHistory;
import org.example.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TranServiceImpl implements TranService {

    @Autowired
    private TranDao tranDao;

    @Autowired
    private TranHistoryDao tranHistoryDao;

    @Autowired
    private CustomerDao customerDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,
            readOnly = false,rollbackFor = {CreateTranException.class})
    public boolean save(Tran tran, String customerName) {
        Customer customer = retCustomer(customerName,tran);
        String customerId = customer.getId();

        tran.setCustomerId(customerId);
        int nums = tranDao.save(tran);
        if (nums != 1){
            throw new CreateTranException("创建交易失败");
        }

        createTranHistory(tran);

        return true;
    }

    @Override
    public PaginationVO<Tran> pageList(Integer pageNo, Integer pageSize, Map<String, Object> map) {
        Page p = PageHelper.startPage(pageNo,pageSize);
        List<Tran> tranList = tranDao.getTranByCondition(map);
        Long total = p.getTotal();

        PaginationVO<Tran> vo = new PaginationVO<>();
        vo.setTotal(total);
        vo.setDataList(tranList);
        return vo;
    }

    @Override
    public Tran detail(String id) {
        Tran tran = tranDao.detail(id);
        return tran;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {

        List<TranHistory> tranHistoryList = tranHistoryDao.getHistoryListByTranId(tranId);
        return tranHistoryList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,
            readOnly = false,rollbackFor = {CreateTranException.class})
    public boolean changeStage(Tran tran) {
        int nums = tranDao.changeStage(tran);
        if (nums != 1){
            throw new CreateTranException("改变交易失败");
        }
        // 生成交易历史
        createTranHistory(tran);
        return true;
    }

    @Override
    public Map<String, Object> getCharts() {
        Map<String, Object> map = new HashMap<>(16);
        // 取得total
        int total = tranDao.getTotal();

        // 取得dataList
        List<Map<String,Object>> dataList = tranDao.getCharts();

        map.put("total",total);
        map.put("dataList",dataList);

        return map;
    }

    private void createTranHistory(Tran tran) {
        String createBy = tran.getCreateBy();
        String createTime = tran.getCreateTime();
        if (tran.getEditTime() != null && tran.getEditTime() != ""){
            createBy = tran.getEditBy();
            createTime = tran.getEditTime();
        }
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setCreateBy(createBy);
        tranHistory.setCreateTime(createTime);
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setTranId(tran.getId());

        int nums = tranHistoryDao.save(tranHistory);
        if (nums != 1){
            throw new CreateTranException("创建交易历史失败");
        }
    }

    private Customer retCustomer(String customerName,Tran tran) {
       Customer customer = customerDao.getCustomerByName(customerName);
       if (customer == null){
           customer = new Customer();

           customer.setId(UUIDUtil.getUUID());
           customer.setOwner(tran.getOwner());
           customer.setName(customerName);
           customer.setCreateBy(tran.getCreateBy());
           customer.setCreateTime(tran.getCreateTime());
           customer.setContactSummary(tran.getContactSummary());
           customer.setNextContactTime(tran.getNextContactTime());
           customer.setDescription(tran.getDescription());

           int nums = customerDao.save(customer);
           if (nums != 1){
               throw new CreateTranException("创建客户失败");
           }

       }
       return customer;
    }
}
