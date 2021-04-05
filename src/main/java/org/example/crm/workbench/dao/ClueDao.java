package org.example.crm.workbench.dao;


import org.example.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {

    int save(Clue c);

    Clue detail(String id);

    List<Clue> getActivityByCondition(Map<String, Object> map);

    Clue getById(String clueId);

    int deleteById(String clueId);
}
