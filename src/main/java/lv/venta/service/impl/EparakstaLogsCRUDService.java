package lv.venta.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CacheEvict;

import lv.venta.model.EParakstaLogs;
import lv.venta.model.Sertifikati;
import lv.venta.repo.IEParakstaLogsRepo;
import lv.venta.repo.ISertifikatiRepo;
import lv.venta.service.IEParakstaLogsService;



@Slf4j
@Service
public  class EparakstaLogsCRUDService implements IEParakstaLogsService {


    @Autowired
    private IEParakstaLogsRepo eParakstaLogsRepo;

    @Autowired
    private ISertifikatiRepo sertifikatiRepo;


    //retrieve all
    @Override
    @Cacheable(value = "eparakstaLogs", unless = "#result == null || #result.isEmpty()")
    public ArrayList<EParakstaLogs> retrieveAllEParakstaLogs() throws Exception {
    	log.info("Sākam izgūt visus e-paraksta logus");
        
        long count = eParakstaLogsRepo.count();
        log.debug("Datubāzē ir {} e-paraksta logi", count);
        
        if (eParakstaLogsRepo.count() == 0) {
        	log.warn("Nav pieejams neviens e-paraksta logs datubāzē");
            throw new Exception("Nav pieejams neviens eParaksta logs");
        }
        ArrayList<EParakstaLogs> logs = (ArrayList<EParakstaLogs>) eParakstaLogsRepo.findAll();
        log.info("Veiksmīgi izgūti {} e-paraksta logi", logs.size());
        
        return logs;
    }


    @Override
    @Cacheable(value = "eparakstaLog", key = "#id", unless = "#result == null")
    public EParakstaLogs retrieveEParakstaLogById(int id) throws Exception {
    	log.info("Mēģina atrast e-paraksta logu ar ID: {}", id);
        if (id <= 0) {
        	log.error("Nederīgs e-paraksta loga ID: {}", id);
            throw new Exception("ID nevar būt negatīvs vai nulle");
        }
        if (!eParakstaLogsRepo.existsById((long) id)) {
        	log.warn("E-paraksta logs ar ID {} neeksistē", id);
            throw new Exception("EParaksta logs ar ID " + id + " neeksistē");
        }
        EParakstaLogs eparakstaLogs = eParakstaLogsRepo.findById((long) id).get();
        log.info("Veiksmīgi atrasts e-paraksta logs: ID={}, Statuss={}, Sertifikāta ID={}", 
            id, eparakstaLogs.getStatuss(), eparakstaLogs.getSertifikati().getSertId());
        
        return eparakstaLogs;
    }


    @Override
    public void updateById(int id, Sertifikati sertifikati, LocalDate parakstisanasDatums, String statuss) throws Exception {
    if (sertifikati == null || parakstisanasDatums == null || statuss == null) {
        throw new Exception("Ievades parametri nav pareizi");
    }
        EParakstaLogs retrievedLog = retrieveEParakstaLogById(id);
        log.debug("Atjauninām e-paraksta loga datus: vecais statuss='{}', jaunais='{}'", 
                retrievedLog.getStatuss(), statuss);
        
        retrievedLog.setSertifikati(sertifikati);
        retrievedLog.setParakstisanasDatums(parakstisanasDatums);
        retrievedLog.setStatuss(statuss);
        eParakstaLogsRepo.save(retrievedLog);
        log.info("Veiksmīgi atjaunināts e-paraksta logs ar ID: {}", id);
    }
    
    
    
    @Override
    @Caching(evict = {
            @CacheEvict(value = "eparakstaLogs", allEntries = true),
            @CacheEvict(value = "eparakstaLog", key = "#id")
        })
    public void deleteMacibuRezultatiById(int id) throws Exception {
    	log.info("Sākam dzēst e-paraksta logu ar ID: {}", id);
        if (id <= 0) {
        	log.error("Nederīgs ID dzēšanai: {}", id);
            throw new Exception("ID nevar būt negatīvs vai nulle");
        }
        if (!eParakstaLogsRepo.existsById((long) id)) {
        	log.warn("Nevar dzēst - e-paraksta logs ar ID {} neeksistē", id);
            throw new Exception("EParaksta logs ar ID " + id + " neeksistē");
        }
        eParakstaLogsRepo.deleteById((long) id);
        log.info("Veiksmīgi izdzēsts e-paraksta logs ar ID: {}", id);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "eparakstaLogs", allEntries = true),
            @CacheEvict(value = "eparakstaLog", key = "#result.eId"),
            @CacheEvict(value = "sertifikats", key = "#sertId")
        })
    public EParakstaLogs insertNewEParakstaLogs(long sertId, String parakstisanasDatums, String statuss) throws Exception {
    	log.info("Sākam pievienot jaunu e-paraksta logu: sertID={}, statuss={}", sertId, statuss);
    	if (sertId <= 0) {
    		log.error("Nederīgs sertifikāta ID: {}", sertId);
            throw new Exception("ID nevar būt negatīvs vai nulle");
        }
        
        if (!sertifikatiRepo.existsById(sertId)) {
        	log.error("Sertifikāts ar ID {} neeksistē", sertId);
            throw new Exception("Sertifikāts ar ID " + sertId + " neeksistē");
        }
        
        Sertifikati sert = sertifikatiRepo.findById(sertId).get();
        LocalDate date = LocalDate.parse(parakstisanasDatums);
        
        log.debug("Izveidojam e-paraksta logu sertifikātam ID: {}, datums: {}, statuss: {}", 
                sertId, date, statuss);
        
        EParakstaLogs newLog = new EParakstaLogs(sert, date, statuss);
        EParakstaLogs savedLog = eParakstaLogsRepo.save(newLog);
        
        log.info("Veiksmīgi pievienots jauns e-paraksta logs ar ID: {}", savedLog.getEId());
        
        return savedLog;
    }
}
    
