package lv.venta.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.venta.model.EParakstaLogs;
import lv.venta.model.Sertifikati;
import lv.venta.repo.IEParakstaLogsRepo;
import lv.venta.service.IEParakstaLogsService;




@Service
public  class EparakstaLogsCRUDService implements IEParakstaLogsService {


    @Autowired
    private IEParakstaLogsRepo eParakstaLogsRepo;


    //retrieve all
    @Override
    public ArrayList<EParakstaLogs> retrieveAllEParakstaLogs() throws Exception {
        if (eParakstaLogsRepo.count() == 0) {
            throw new Exception("Nav pieejami neviens eParaksta logs");
        }
        return (ArrayList<EParakstaLogs>) eParakstaLogsRepo.findAll();
    }


    @Override
    public EParakstaLogs retrieveEParakstaLogById(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID nevar būt negatīvs vai nulle");
        }
        if (!eParakstaLogsRepo.existsById((long) id)) {
            throw new Exception("EParaksta logs ar ID " + id + " neeksistē");
        }
        return eParakstaLogsRepo.findById((long) id).get();
    }


    @Override
    public void updateById(int id, Sertifikati sertifikati, LocalDate parakstisanasDatums, String statuss) throws Exception {
    if (sertifikati == null || parakstisanasDatums == null || statuss == null) {
        throw new Exception("Ievades parametri nav pareizi");
    }
        EParakstaLogs retrievedLog = retrieveEParakstaLogById(id);
        retrievedLog.setSertifikati(sertifikati);
        retrievedLog.setParakstisanasDatums(parakstisanasDatums);
        retrievedLog.setStatuss(statuss);
        eParakstaLogsRepo.save(retrievedLog);
    }
    
    
    
    @Override
    public void deleteMacibuRezultatiById(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID nevar būt negatīvs vai nulle");
        }
        if (!eParakstaLogsRepo.existsById((long) id)) {
            throw new Exception("EParaksta logs ar ID " + id + " neeksistē");
        }
        eParakstaLogsRepo.deleteById((long) id);
    }


    



}
