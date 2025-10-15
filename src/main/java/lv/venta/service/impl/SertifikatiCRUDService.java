package lv.venta.service.impl;

import lv.venta.model.KursaDalibnieks;
import lv.venta.model.KursaDatumi;
import lv.venta.model.Sertifikati;
import lv.venta.repo.IKursaDalibnieksRepo;
import lv.venta.repo.IKursaDatumiRepo;
import lv.venta.repo.ISertifikatiRepo;
import lv.venta.service.ISertifikatiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class SertifikatiCRUDService implements ISertifikatiService {

    @Autowired
    private ISertifikatiRepo sertRepo;
    
    @Autowired
    private IKursaDalibnieksRepo dalibnieksRepo;
    
    @Autowired
    private IKursaDatumiRepo datumiRepo;

    @Override
    @Cacheable(value = "sertifikati", unless = "#result == null || #result.isEmpty()")
    public ArrayList<Sertifikati> retrieveAllSertifikati() throws Exception {
        if (sertRepo.count() == 0) {
            throw new Exception("Nav pieejams neviens sertifikāts");
        }
        return (ArrayList<Sertifikati>) sertRepo.findAll();
    }

    @Override
    @Cacheable(value = "sertifikats", key = "#sertId", unless = "#result == null")
    public Sertifikati retrieveSertifikatiById(long sertId) throws Exception {
        if (sertId <= 0) {
            throw new Exception("ID nevar būt negatīvs vai nulle");
        }
        if (!sertRepo.existsById(sertId)) {
            throw new Exception("Sertifikāts ar ID " + sertId + " neeksistē");
        }
        return sertRepo.findById(sertId).get();
    }


    @Override
    @Caching(evict = {
            @CacheEvict(value = "sertifikati", allEntries = true),
            @CacheEvict(value = "sertifikats", key = "#result.sertId")
        })
    public Sertifikati insertNewSertifikats(long kdId, long kdatId, String izsniegtsDatums, boolean parakstits) throws Exception {
        if (kdId <= 0 || kdatId <= 0) {
            throw new Exception("ID nevar būt negatīvs vai nulle");
        }
        
        if (!dalibnieksRepo.existsById(kdId)) {
            throw new Exception("Kursa dalībnieks ar ID " + kdId + " neeksistē");
        }
        
        if (!datumiRepo.existsById(kdatId)) {
            throw new Exception("Kursa datumi ar ID " + kdatId + " neeksistē");
        }
        
        KursaDalibnieks dalibnieks = dalibnieksRepo.findById(kdId).get();
        KursaDatumi datumi = datumiRepo.findById(kdatId).get();
        
        LocalDate date = LocalDate.parse(izsniegtsDatums);
        
        Sertifikati newSert = new Sertifikati(dalibnieks, datumi, date, parakstits);
        return sertRepo.save(newSert);
    }
    }
