package lv.venta.service;

import java.time.LocalDate;
import java.util.ArrayList;

import lv.venta.model.EParakstaLogs;
import lv.venta.model.sertifikati;

public interface IEParakstaLogsService {

    ArrayList<EParakstaLogs> retrieveAllEParakstaLogs() throws Exception;
    
    EParakstaLogs retrieveEParakstaLogById(int id) throws Exception;

    void deleteMacibuRezultatiById(int id) throws Exception;
    
    void create(sertifikati sertifikati, LocalDate parakstisanasDatums, String statuss) throws Exception;
}
