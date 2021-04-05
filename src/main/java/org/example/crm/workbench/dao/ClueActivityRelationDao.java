package org.example.crm.workbench.dao;

import org.example.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {


    /**
     *
     * @param id
     * @return
     */
    int unbind(String id);


    int save(ClueActivityRelation car);

    /**
     * 根据线索Id查找 ClueActivityRelation
     * @param clueId
     * @return
     */
    List<ClueActivityRelation> getListByClueId(String clueId);
}
