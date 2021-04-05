package org.example.crm.workbench.service;

import org.example.crm.vo.PaginationVO;
import org.example.crm.workbench.domain.*;

import java.util.List;
import java.util.Map;

public interface ClueService {

    /**
     * 线索保存
     *
     * @param clue
     * @return
     */
    boolean save(Clue clue);

    /**
     *
     * @param pageNo
     * @param pageSize
     * @param map
     * @return
     */
    PaginationVO<Clue> pageList(Integer pageNo, Integer pageSize, Map<String, Object> map);

    /**
     *
     * @param id
     * @return
     */
    Clue detail(String id);

    /**
     * 根据id删除线索市场关系表
     * @param id
     * @return
     */
    boolean unbind(String id);

    /**
     *
     * 关联市场活动
     *
     * @param aids
     * @param cid
     * @return
     */
    boolean bind(String[] aids, String cid);

    /**
     * 市场活动转换
     *
     * @param clueId
     * @param tran
     * @param creatBy
     * @return
     */
    boolean convert(String clueId, Tran tran,String creatBy);

    /**
     * 线索备注保存
     *
     * @param clueRemark
     * @return
     */
    boolean saveRemark(ClueRemark clueRemark);

    /**
     * 根据线索id查找线索备注
     *
     * @param clueId
     * @return
     */
    List<ClueRemark> getRemarkListByCid(String clueId);

    /**
     * 更新市场活动备注
     *
     * @param clueRemark
     * @return
     */
    boolean updateRemark(ClueRemark clueRemark);

    /**
     * 根据id删除线索备注
     * @param id
     * @return
     */
    boolean deleteClueRemark(String id);
}
