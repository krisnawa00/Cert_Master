package lv.venta.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import lv.venta.model.Kurss;
import lv.venta.model.MacibuRezultati;
import lv.venta.repo.IKurssRepo;
import lv.venta.repo.IMacibuRezultatiRepo;
import lv.venta.service.IMacibuRezultatiService;
import org.springframework.cache.annotation.CacheEvict;


@Slf4j
@Service
public class MacibuRezultatiCrudService implements IMacibuRezultatiService {

    @Autowired
    private IMacibuRezultatiRepo macibuRezultatiRepo; 
    
    
    @Autowired
    private IKurssRepo kurssRepo;

    //retrieve all
    @Override
    @Cacheable(value = "macibuRezultati", unless = "#result == null || #result.isEmpty()")
    public ArrayList<MacibuRezultati> retrieveAllMacibuRezultati() throws Exception {
    	log.info("Sākam izgūt visus mācību rezultātus");
        
        long count = macibuRezultatiRepo.count();
        log.debug("Datubāzē ir {} mācību rezultāti", count);
    	if (macibuRezultatiRepo.count() == 0) {
    		log.warn("Nav pieejami neviens mācību rezultāts datubāzē");
            throw new Exception("Nav pieejami neviens macību rezultāts");
        }
    	ArrayList<MacibuRezultati> rezultati = (ArrayList<MacibuRezultati>) macibuRezultatiRepo.findAll();
        log.info("Veiksmīgi izgūti {} mācību rezultāti", rezultati.size());
        
        return rezultati;
    }

    @Override
    @Cacheable(value = "macibuRezultats", key = "#id", unless = "#result == null")
    public MacibuRezultati retrieveMacibuRezultatiById(int id) throws Exception {
    	log.info("Mēģina atrast mācību rezultātu ar ID: {}", id);
        if (id <= 0) {
        	log.error("Nederīgs mācību rezultāta ID: {}", id);
            throw new Exception("ID nevar būt negatīvs vai nulle");
        }
        if (!macibuRezultatiRepo.existsById((long) id)) {
        	log.warn("Mācību rezultāts ar ID {} neeksistē", id);
            throw new Exception("Macību rezultāts ar ID " + id + " neeksistē");
        }
        MacibuRezultati rezultats = macibuRezultatiRepo.findById((long) id).get();
        log.info("Veiksmīgi atrasts mācību rezultāts: ID={}, Kurss={}, Iziets={}", 
            id, 
            rezultats.getKurss().getNosaukums(), 
            rezultats.isMacibuRezultats());
        
        return rezultats;
    }
    
    @Override
    public void updateById(int id, Kurss kurss, boolean macibuRezultats) throws Exception {
    if (kurss == null) {
    	log.error("Nederīgi parametri mācību rezultāta atjaunināšanai: kurss=null");
        throw new Exception("Ievades parametri nav pareizi");
    }
    MacibuRezultati retrievedMacR = retrieveMacibuRezultatiById(id);
    log.debug("Atjauninām mācību rezultāta datus: vecais status={}, jaunais={}", 
            retrievedMacR.isMacibuRezultats(), macibuRezultats);
    
    retrievedMacR.setKurss(kurss);
    retrievedMacR.setMacibuRezultats(macibuRezultats);
    macibuRezultatiRepo.save(retrievedMacR);
    log.info("Veiksmīgi atjaunināts mācību rezultāts ar ID: {}", id);
}


    
    @Override
    @Caching(evict = {
            @CacheEvict(value = "macibuRezultati", allEntries = true),
            @CacheEvict(value = "macibuRezultats", key = "#id")
        })
    public void deleteMacibuRezultatiById(int id) throws Exception {
    	log.info("Sākam dzēst mācību rezultātu ar ID: {}", id);
        if (id <= 0) {
        	log.error("Nederīgs ID dzēšanai: {}", id);
            throw new Exception("ID nevar būt negatīvs vai nulle");
        }
        if (!macibuRezultatiRepo.existsById((long) id)) {
        	log.warn("Nevar dzēst - mācību rezultāts ar ID {} neeksistē", id);
            throw new Exception("Macību rezultāts ar ID " + id + " neeksistē");
        }
        macibuRezultatiRepo.deleteById((long) id);
        log.info("Veiksmīgi izdzēsts mācību rezultāts ar ID: {}", id);
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = "macibuRezultati", allEntries = true),
        @CacheEvict(value = "macibuRezultats", key = "#result.mrId")
    })
    public MacibuRezultati insertNewMacibuRezultats(long kId, boolean macibuRezultats) throws Exception {
    	log.info("Sākam pievienot jaunu mācību rezultātu: kursID={}, iziets={}", kId, macibuRezultats);
    	if (kId <= 0) {
    		log.error("Nederīgs kursa ID: {}", kId);
            throw new Exception("ID nevar būt negatīvs vai nulle");
        }
        
        if (!kurssRepo.existsById(kId)) {
        	log.error("Kurss ar ID {} neeksistē", kId);
            throw new Exception("Kurss ar ID " + kId + " neeksistē");
        }
        
        Kurss kurss = kurssRepo.findById(kId).get();
        log.debug("Izveidojam mācību rezultātu kursam: {}, iziets: {}", 
                kurss.getNosaukums(), macibuRezultats);
        
        MacibuRezultati newRezultats = new MacibuRezultati(kurss, macibuRezultats);
        MacibuRezultati savedRezultats = macibuRezultatiRepo.save(newRezultats);
        
        log.info("Veiksmīgi pievienots jauns mācību rezultāts ar ID: {}", savedRezultats.getMrId());
        
        return savedRezultats;
    }
}
    
    

    


    
