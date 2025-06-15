package lv.venta.service;

import java.util.ArrayList;

import lv.venta.model.EParakstaLogs;


public interface IEParakstaLogsService {



    ArrayList<EParakstaLogs> retrieveAllEParakstaLogs() throws Exception;
    
    EParakstaLogs retrieveEParakstaLogById(int id) throws Exception;

    void deleteMacibuRezultatiById(int id) throws Exception;

    

    

    

}
