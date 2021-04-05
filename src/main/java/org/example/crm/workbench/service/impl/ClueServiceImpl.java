package org.example.crm.workbench.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.crm.exception.ActivityDeleteException;
import org.example.crm.exception.ClueActivityRelationException;
import org.example.crm.exception.ConvertException;
import org.example.crm.utils.DateTimeUtil;
import org.example.crm.utils.UUIDUtil;
import org.example.crm.vo.PaginationVO;
import org.example.crm.workbench.dao.*;
import org.example.crm.workbench.domain.*;
import org.example.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Service
public class ClueServiceImpl implements ClueService {

    @Autowired
    private ClueDao clueDao;

    @Autowired
    private ClueActivityRelationDao clueActivityRelationDao;

    @Autowired
    private ClueRemarkDao clueRemarkDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private ContactsDao contactsDao;

    @Autowired
    private CustomerRemarkDao customerRemarkDao;

    @Autowired
    private ContactsRemarkDao contactsRemarkDao;

    @Autowired
    private ContactsActivityRelationDao contactsActivityRelationDao;

    @Autowired
    private TranDao tranDao;

    @Autowired
    private TranHistoryDao tranHistoryDao;

    @Override
    public boolean save(Clue clue) {
        boolean flag = false;
        int nums = clueDao.save(clue);
        if (nums == 1){
            flag = true;
        }
        return flag;
    }

    @Override
    public PaginationVO<Clue> pageList(Integer pageNo, Integer pageSize, Map<String, Object> map) {
        Page p = PageHelper.startPage(pageNo,pageSize);
        List<Clue> clueList = clueDao.getActivityByCondition(map);
        Long total = p.getTotal();

        PaginationVO<Clue> vo = new PaginationVO<>();
        vo.setTotal(total);
        vo.setDataList(clueList);
        return vo;
    }

    @Override
    public Clue detail(String id) {
        Clue clue = clueDao.detail(id);
        return clue;
    }

    @Override
    public boolean unbind(String id) {
        boolean flag = false;
        int nums = clueActivityRelationDao.unbind(id);
        if (nums == 1){
            flag = true;
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,
            readOnly = false,rollbackFor = {ActivityDeleteException.class})
    public boolean bind(String[] aids, String cid) {

        for (String aid : aids){
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(cid);
            car.setActivityId(aid);

            int nums = clueActivityRelationDao.save(car);
            if (nums != 1){
                throw new ClueActivityRelationException("关联市场活动失败");
            }
        }

        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,
            readOnly = false,rollbackFor = {ConvertException.class})
    public boolean convert(String clueId, Tran tran,String creatBy) {

        String creatTime = DateTimeUtil.getSysTime();

        // (1) 获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue clue = clueDao.getById(clueId);

        // (2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        Customer customer = retCustomer(clue,creatBy,creatTime);


        // (3) 通过线索对象提取联系人信息，保存联系人
        String customerId = customer.getId();
        Contacts contacts = retContacts(clue,creatBy,creatTime,customerId);

        // (4) 线索备注转换到客户备注以及联系人备注  (8) 删除线索备注
        String contactsId = contacts.getId();
        convertRemark(clueId,contactsId,customerId,creatBy,creatTime);

        // (5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系  (9) 删除线索和市场活动的关系
        convertRelation(clueId,contactsId);

        // (6) 如果有创建交易需求，创建一条交易
        if (tran != null){
            // (7) 如果创建了交易，则创建一条该交易下的交易历史
            createTran(tran,clue,customerId,contactsId,creatTime,creatBy);
        }

        // (10) 删除线索
        int nums = clueDao.deleteById(clueId);
        if (nums != 1){
            throw new ConvertException("删除线索失败");
        }
        return true;
    }

    public void createTran(Tran tran, Clue clue, String customerId, String contactsId, String createTime, String createBy) {
        tran.setId(UUIDUtil.getUUID());
        tran.setSource(clue.getSource());
        tran.setOwner(clue.getOwner());
        tran.setNextContactTime(clue.getNextContactTime());
        tran.setDescription(clue.getDescription());
        tran.setCustomerId(customerId);
        tran.setContactSummary(clue.getContactSummary());
        tran.setContactsId(contactsId);

        int nums = tranDao.save(tran);
        if (nums != 1){
            throw new ConvertException("交易创建失败");
        }

        // (7) 如果创建了交易，则创建一条该交易下的交易历史
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setCreateBy(createBy);
        tranHistory.setCreateTime(createTime);
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setStage(tran.getStage());
        tranHistory.setTranId(tran.getId());

        int count = tranHistoryDao.save(tranHistory);
        if (count != 1){
            throw new ConvertException("创建交易历史失败");
        }

    }

    public void convertRelation(String clueId, String contactsId) {
        // 查询出与该条线索关联的市场活动，查询与市场活动的关联关系列表
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        for (ClueActivityRelation clueActivityRelation:clueActivityRelationList){
            String activityId = clueActivityRelation.getActivityId();

            // 创建联系人与市场活动的关联关系对象，让第三步生成的联系人与市场活动做关联
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(contactsId);
            //添加联系人与市场活动的关联关系
            int nums = contactsActivityRelationDao.save(contactsActivityRelation);
            if (nums != 1){
                throw new ConvertException("线索和市场活动的关系转换到联系人和市场活动的关系失败");
            }

            // (9) 删除线索和市场活动的关系
            String clueActivityRelationId = clueActivityRelation.getId();
            int count = clueActivityRelationDao.unbind(clueActivityRelationId);
            if (count != 1){
                throw new ConvertException("删除线索和市场活动的关系失败");
            }
        }
    }

    public void convertRemark(String clueId,String contactsId,String customerId,String createBy,String createTime){
        List<ClueRemark> clueRemarkList = clueRemarkDao.getRemarkListByCid(clueId);
        for (ClueRemark clueRemark: clueRemarkList) {
            // 取出备注信息
            String noteContent = clueRemark.getNoteContent();

            // 创建客户备注对象，添加客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(customerId);
            customerRemark.setEditFlag("0");
            customerRemark.setNoteContent(noteContent);
            int nums = customerRemarkDao.save(customerRemark);
            if (nums != 1){
                throw new ConvertException("客户备注信息添加失败");
            }

            //创建联系人备注对象，添加联系人
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setContactsId(contactsId);
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(noteContent);
            int count = contactsRemarkDao.save(contactsRemark);
            if (count != 1){
                throw new ConvertException("联系人备注添加失败");
            }

            // (8) 删除线索备注
            String clueRemarkId = clueRemark.getId();
            int num = clueRemarkDao.deleteById(clueRemarkId);
            if (num != 1){
                throw new ConvertException("线索备注删除失败");
            }

        }

    }

    public Contacts retContacts(Clue clue, String creatBy, String creatTime, String customerId) {
        String fullname = clue.getFullname();
        Map<String,Object> map = new HashMap<>(16);
        map.put("fullname",fullname);
        map.put("customerId",customerId);
        Contacts contacts = contactsDao.getContactsByCondition(map);
        if (contacts == null){
            contacts = new Contacts();
            contacts.setId(UUIDUtil.getUUID());
            contacts.setCreateBy(creatBy);
            contacts.setCreateTime(creatTime);
            contacts.setOwner(clue.getOwner());
            contacts.setSource(clue.getSource());
            contacts.setCustomerId(customerId);
            contacts.setFullname(clue.getFullname());
            contacts.setAppellation(clue.getAppellation());
            contacts.setEmail(clue.getEmail());
            contacts.setMphone(clue.getMphone());
            contacts.setJob(clue.getJob());
            contacts.setDescription(clue.getDescription());
            contacts.setContactSummary(clue.getContactSummary());
            contacts.setNextContactTime(clue.getNextContactTime());
            contacts.setAddress(clue.getAddress());

            int nums = contactsDao.save(contacts);
            if (nums != 1){
                throw new ConvertException("添加客户信息失败");
            }
        }
        
        return contacts;
    }

    public Customer retCustomer(Clue clue, String creatBy, String creatTime) {
        // 查询是否存在该顾客（公司） 通过公司名称进行匹配
        String company = clue.getCompany();
        Customer customer = customerDao.getCustomerByName(company);

        if (customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setCreateBy(creatBy);
            customer.setCreateTime(creatTime);
            customer.setOwner(clue.getOwner());
            customer.setName(clue.getCompany());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setContactSummary(clue.getContactSummary());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setDescription(clue.getDescription());
            customer.setAddress(clue.getAddress());

            int nums = customerDao.save(customer);
            if (nums != 1){
                throw new ConvertException("添加客户信息失败");
            }
        }
        return customer;
    }

    @Override
    public boolean saveRemark(ClueRemark clueRemark) {
        boolean flag = false;
        int nums = clueRemarkDao.save(clueRemark);
        if (nums == 1){
            flag = true;
        }
        return flag;
    }

    @Override
    public List<ClueRemark> getRemarkListByCid(String clueId) {
        List<ClueRemark> arList = clueRemarkDao.getRemarkListByCid(clueId);
        // 对返回的数据进行重排序
        arList.sort(new Comparator<ClueRemark>() {
            @Override
            public int compare(ClueRemark a1, ClueRemark a2) {
                String t1 = "0".equals(a1.getEditFlag()) ? a1.getCreateTime() : a1.getEditTime();
                String t2 = "0".equals(a2.getEditFlag()) ? a2.getCreateTime() : a2.getEditTime();

                return t1.compareTo(t2);
            }
        });
        return arList;
    }

    @Override
    public boolean updateRemark(ClueRemark clueRemark) {
        boolean flag = false;
        int nums = clueRemarkDao.update(clueRemark);
        if (nums == 1){
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean deleteClueRemark(String id) {
        boolean flag = false;
        int nums = clueRemarkDao.deleteById(id);
        if (nums == 1){
            flag = true;
        }
        return flag;
    }


}
