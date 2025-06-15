package lv.venta.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.venta.model.EParakstaLogs;
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
