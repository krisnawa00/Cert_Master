package lv.venta.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lv.venta.model.EParakstaLogs;
import lv.venta.model.Sertifikati;
import lv.venta.repo.IEParakstaLogsRepo;
import lv.venta.repo.ISertifikatiRepo;
import lv.venta.service.IEParakstaLogsService;

@Service
public class EparakstaLogsCRUDService implements IEParakstaLogsService {

    @Autowired
    private IEParakstaLogsRepo eParakstaLogsRepo;

    @Autowired
    private ISertifikatiRepo sertifikatiRepo;

    // Pagination metode
    public Page<EParakstaLogs> retrieveAllEParakstaLogsPaginated(Pageable pageable) throws Exception {
        Page<EParakstaLogs> page = eParakstaLogsRepo.findAll(pageable);
        if (page.isEmpty()) {
            throw new Exception("Nav pieejami neviens eParaksta logs");
        }
        return page;
    }

    @Override
    @Cacheable(value = "eparakstaLogs", unless = "#result == null || #result.isEmpty()")
    public ArrayList<EParakstaLogs> retrieveAllEParakstaLogs() throws Exception {
        if (eParakstaLogsRepo.count() == 0) {
            throw new Exception("Nav pieejami neviens eParaksta logs");
        }
        return (ArrayList<EParakstaLogs>) eParakstaLogsRepo.findAll();
    }

    @Override
    @Cacheable(value = "eparakstaLog", key = "#id", unless = "#result == null")
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
    @Caching(evict = {
            @CacheEvict(value = "eparakstaLogs", allEntries = true),
            @CacheEvict(value = "eparakstaLog", key = "#id")
        })
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
    @Caching(evict = {
            @CacheEvict(value = "eparakstaLogs", allEntries = true),
            @CacheEvict(value = "eparakstaLog", key = "#id")
        })
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
    @Caching(evict = {
            @CacheEvict(value = "eparakstaLogs", allEntries = true),
            @CacheEvict(value = "eparakstaLog", key = "#result.eId"),
            @CacheEvict(value = "sertifikats", key = "#sertId")
        })
    public EParakstaLogs insertNewEParakstaLogs(long sertId, String parakstisanasDatums, String statuss) throws Exception {
        if (sertId <= 0) {
            throw new Exception("ID nevar būt negatīvs vai nulle");
        }
        
        if (!sertifikatiRepo.existsById(sertId)) {
            throw new Exception("Sertifikāts ar ID " + sertId + " neeksistē");
        }
        
        Sertifikati sert = sertifikatiRepo.findById(sertId).get();
        LocalDate date = LocalDate.parse(parakstisanasDatums);
        
        EParakstaLogs newLog = new EParakstaLogs(sert, date, statuss);
        return eParakstaLogsRepo.save(newLog);
    }
}