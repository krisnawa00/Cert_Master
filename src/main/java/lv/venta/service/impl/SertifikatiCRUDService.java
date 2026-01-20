package lv.venta.service.impl;

import lv.venta.model.KursaDalibnieks;
import lv.venta.model.KursaDatumi;
import lv.venta.model.Sertifikati;
import lv.venta.repo.IKursaDalibnieksRepo;
import lv.venta.repo.IKursaDatumiRepo;
import lv.venta.repo.ISertifikatiRepo;
import lv.venta.service.ISertifikatiService;

import java.time.LocalDate;
import java.util.ArrayList;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



import lv.venta.repo.IEParakstaLogsRepo;
import lv.venta.repo.ISertRegTab;


@Service
public class SertifikatiCRUDService implements ISertifikatiService {

    @Autowired
    private ISertifikatiRepo sertRepo;
    
    @Autowired
    private IKursaDalibnieksRepo dalibnieksRepo;
    
    @Autowired
    private IKursaDatumiRepo datumiRepo;

    @Autowired
    private IEParakstaLogsRepo eparakstaLogsRepo;

    @Autowired
    private ISertRegTab sertRegTabRepo;

    public Page<Sertifikati> retrieveAllSertifikatiPaginated(Pageable pageable) throws Exception {
        Page<Sertifikati> page = sertRepo.findAll(pageable);
        if (page.isEmpty()) {
            throw new Exception("Nav pieejams neviens sertifikāts");
        }
        return page;
    }
    
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
    

    public void updateById(int id,KursaDalibnieks dalibnieks, KursaDatumi kursaDatums,LocalDate izsniegtsDatums,boolean parakstits)throws Exception
    {
        if (dalibnieks == null || kursaDatums == null || izsniegtsDatums == null) {
        throw new Exception("Ievades parametri nav pareizi");
    }
        Sertifikati retrievedSert = retrieveSertifikatiById(id);
        retrievedSert.setDalibnieks(dalibnieks);
        retrievedSert.setKursaDatums(kursaDatums);
        retrievedSert.setIzsniegtsDatums(izsniegtsDatums);
        retrievedSert.setParakstits(parakstits);
        sertRepo.save(retrievedSert);


    }
    
    
    
    
    
    public void deleteSertifikatiById(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID nevar būt negatīvs vai nulle");
        }
        if (!sertRepo.existsById((long) id)) {
            throw new Exception("Sertifikāts ar ID " + id + " neeksistē");
        }
        Sertifikati certificate = sertRepo.findById((long) id).get();
        
        sertRegTabRepo.deleteAll(certificate.getSertifikatuRegistracijasTabula());
        eparakstaLogsRepo.deleteAll(certificate.getEparakstsLogs());
        
        sertRepo.deleteById((long) id);
    }








}