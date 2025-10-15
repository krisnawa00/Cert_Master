package lv.venta.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.venta.model.EParakstaLogs;
import lv.venta.model.sertifikati;
import lv.venta.repo.IEParakstaLogsRepo;
import lv.venta.service.IEParakstaLogsService;

@Service
public class EparakstaLogsCRUDService implements IEParakstaLogsService {

    @Autowired
    private IEParakstaLogsRepo eParakstaLogsRepo;

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

    @Override
    public void create(sertifikati sertifikati, LocalDate parakstisanasDatums, String statuss) throws Exception {
        if (sertifikati == null || parakstisanasDatums == null || statuss == null) {
            throw new Exception("Ievades parametri nav pareizi");
        }

        if (parakstisanasDatums.isAfter(LocalDate.now())) {
            throw new Exception("Parakstīšanas datums nevar būt nākotnē");
        }

        if (statuss.trim().isEmpty()) {
            throw new Exception("Statuss nevar būt tukšs");
        }

        EParakstaLogs newLog = new EParakstaLogs(sertifikati, parakstisanasDatums, statuss);
        eParakstaLogsRepo.save(newLog);
        System.out.println("Jauns eParaksta logs izveidots");
    }
}