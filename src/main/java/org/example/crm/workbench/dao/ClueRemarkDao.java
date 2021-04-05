package org.example.crm.workbench.dao;

import org.example.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    int save(ClueRemark clueRemark);

    List<ClueRemark> getRemarkListByCid(String clueId);

    int update(ClueRemark clueRemark);

    int deleteById(String id);

}
