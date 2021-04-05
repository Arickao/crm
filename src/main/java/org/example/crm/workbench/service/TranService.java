package org.example.crm.workbench.service;

import org.example.crm.vo.PaginationVO;
import org.example.crm.workbench.domain.Tran;
import org.example.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {

    boolean save(Tran tran, String customerName);

    PaginationVO<Tran> pageList(Integer pageNo, Integer pageSize, Map<String, Object> map);

    Tran detail(String id);

    List<TranHistory> getHistoryListByTranId(String tranId);

    boolean changeStage(Tran tran);

    Map<String, Object> getCharts();

}
