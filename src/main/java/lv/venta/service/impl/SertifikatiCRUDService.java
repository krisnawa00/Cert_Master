package lv.venta.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import lv.venta.model.KursaDalibnieks;
import lv.venta.model.KursaDatumi;
import lv.venta.model.Sertifikati;
import lv.venta.repo.IEParakstaLogsRepo;
import lv.venta.repo.IKursaDalibnieksRepo;
import lv.venta.repo.IKursaDatumiRepo;
import lv.venta.repo.ISertRegTab;
import lv.venta.repo.ISertifikatiRepo;
import lv.venta.service.ISertifikatiService;

@Slf4j
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
        log.info("Sākam izgūt visus sertifikātus");
        
        long count = sertRepo.count();
        log.debug("Datubāzē ir {} sertifikāti", count);
        
        if (count == 0) {
            log.warn("Nav pieejams neviens sertifikāts datubāzē");
            throw new Exception("Nav pieejams neviens sertifikāts");
        }
        
        ArrayList<Sertifikati> sertifikati = (ArrayList<Sertifikati>) sertRepo.findAll();
        log.info("Veiksmīgi izgūti {} sertifikāti", sertifikati.size());
        
        return sertifikati;
    }

    @Override
    @Cacheable(value = "sertifikats", key = "#sertId", unless = "#result == null")
    public Sertifikati retrieveSertifikatiById(long sertId) throws Exception {
        log.info("Mēģinām atrast sertifikātu ar ID: {}", sertId);
        
        if (sertId <= 0) {
            log.error("Nederīgs sertifikāta ID: {}", sertId);
            throw new Exception("ID nevar būt negatīvs vai nulle");
        }
        
        if (!sertRepo.existsById(sertId)) {
            log.warn("Sertifikāts ar ID {} neeksistē", sertId);
            throw new Exception("Sertifikāts ar ID " + sertId + " neeksistē");
        }
        
        Sertifikati sertifikats = sertRepo.findById(sertId).get();
        log.info("Veiksmīgi atrasts sertifikāts: ID={}, Dalībnieks={} {}", 
            sertId, 
            sertifikats.getDalibnieks().getVards(), 
            sertifikats.getDalibnieks().getUzvards());
        
        return sertifikats;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "sertifikati", allEntries = true),
            @CacheEvict(value = "sertifikats", key = "#result.sertId")
        })
    public Sertifikati insertNewSertifikats(long kdId, long kdatId, String izsniegtsDatums, boolean parakstits) throws Exception {
        log.info("Sākam pievienot jaunu sertifikātu: dalībnieksID={}, kursaDatumiID={}, parakstīts={}", 
            kdId, kdatId, parakstits);
        
        if (kdId <= 0 || kdatId <= 0) {
            log.error("Nederīgi ID parametri: kdId={}, kdatId={}", kdId, kdatId);
            throw new Exception("ID nevar būt negatīvs vai nulle");
        }
        
        if (!dalibnieksRepo.existsById(kdId)) {
            log.error("Kursa dalībnieks ar ID {} neeksistē", kdId);
            throw new Exception("Kursa dalībnieks ar ID " + kdId + " neeksistē");
        }
        
        if (!datumiRepo.existsById(kdatId)) {
            log.error("Kursa datumi ar ID {} neeksistē", kdatId);
            throw new Exception("Kursa datumi ar ID " + kdatId + " neeksistē");
        }
        
        KursaDalibnieks dalibnieks = dalibnieksRepo.findById(kdId).get();
        KursaDatumi datumi = datumiRepo.findById(kdatId).get();
        LocalDate date = LocalDate.parse(izsniegtsDatums);
        
        log.debug("Izveidojam sertifikātu dalībniekam: {} {}, kurss: {}", 
            dalibnieks.getVards(), 
            dalibnieks.getUzvards(),
            datumi.getKurss().getNosaukums());
        
        Sertifikati newSert = new Sertifikati(dalibnieks, datumi, date, parakstits);
        Sertifikati savedSert = sertRepo.save(newSert);
        
        log.info("Veiksmīgi pievienots jauns sertifikāts ar ID: {}", savedSert.getSertId());
        
        return savedSert;
    }
    
    public void updateById(int id, KursaDalibnieks dalibnieks, KursaDatumi kursaDatums, 
                          LocalDate izsniegtsDatums, boolean parakstits) throws Exception {
        log.info("Sākam atjaunināt sertifikātu ar ID: {}", id);
        
        if (dalibnieks == null || kursaDatums == null || izsniegtsDatums == null) {
            log.error("Nederīgi parametri sertifikāta atjaunināšanai: dalibnieks={}, kursaDatums={}, datums={}", 
                dalibnieks != null, kursaDatums != null, izsniegtsDatums != null);
            throw new Exception("Ievades parametri nav pareizi");
        }
        
        Sertifikati retrievedSert = retrieveSertifikatiById(id);
        
        log.debug("Atjauninām sertifikāta datus: vecais parakstīšanas status={}, jaunais={}", 
            retrievedSert.isParakstits(), parakstits);
        
        retrievedSert.setDalibnieks(dalibnieks);
        retrievedSert.setKursaDatums(kursaDatums);
        retrievedSert.setIzsniegtsDatums(izsniegtsDatums);
        retrievedSert.setParakstits(parakstits);
        
        sertRepo.save(retrievedSert);
        log.info("Veiksmīgi atjaunināts sertifikāts ar ID: {}", id);
    }
    
    public void deleteSertifikatiById(int id) throws Exception {
        log.info("Sākam dzēst sertifikātu ar ID: {}", id);
        
        if (id <= 0) {
            log.error("Nederīgs ID dzēšanai: {}", id);
            throw new Exception("ID nevar būt negatīvs vai nulle");
        }
        
        if (!sertRepo.existsById((long) id)) {
            log.warn("Nevar dzēst - sertifikāts ar ID {} neeksistē", id);
            throw new Exception("Sertifikāts ar ID " + id + " neeksistē");
        }
        
        Sertifikati certificate = sertRepo.findById((long) id).get();
        
        log.debug("Dzēšam saistītos ierakstus priekš sertifikāta ID: {}", id);
        
        int regTabCount = certificate.getSertifikatuRegistracijasTabula().size();
        int eparakstCount = certificate.getEparakstsLogs().size();
        
        sertRegTabRepo.deleteAll(certificate.getSertifikatuRegistracijasTabula());
        log.debug("Izdzēsti {} reģistrācijas tabulas ieraksti", regTabCount);
        
        eparakstaLogsRepo.deleteAll(certificate.getEparakstsLogs());
        log.debug("Izdzēsti {} e-paraksta logi", eparakstCount);
        
        sertRepo.deleteById((long) id);
        log.info("Veiksmīgi izdzēsts sertifikāts ar ID: {}", id);
    }
}