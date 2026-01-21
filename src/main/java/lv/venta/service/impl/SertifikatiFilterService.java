package lv.venta.service.impl;

import lv.venta.model.Sertifikati;
import lv.venta.repo.ISertifikatiRepo;
import lv.venta.service.ISertifikatiFilterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;
@Slf4j
@Service
public class SertifikatiFilterService implements ISertifikatiFilterService {

    @Autowired
    private ISertifikatiRepo sertRepo;

    @Override
    public ArrayList<Sertifikati> filterByParakstitStatus(boolean parakstits) throws Exception {
    	log.info("Filtrējam sertifikātus pēc parakstīšanas statusa: {}", parakstits ? "parakstīts" : "neparakstīts");
    	
        ArrayList<Sertifikati> allSerts = (ArrayList<Sertifikati>) sertRepo.findAll();
        
        if (allSerts.isEmpty()) {
        	log.warn("Nav pieejams neviens sertifikāts datubāzē");
            throw new Exception("Nav pieejams neviens sertifikāts");
        }
        
        log.debug("Kopā datubāzē: {} sertifikāti", allSerts.size());
        
        ArrayList<Sertifikati> filtered = allSerts.stream()
                .filter(s -> s.isParakstits() == parakstits)
                .collect(Collectors.toCollection(ArrayList::new));
        
        if (filtered.isEmpty()) {
        	log.warn("Nav atrasti sertifikāti ar statusu: {}", parakstits ? "parakstīts" : "neparakstīts");
            throw new Exception("Nav atrasti sertifikāti ar statusu: " + (parakstits ? "parakstīts" : "neparakstīts"));
        }
        log.info("Filtrēšana pabeigta: atrasti {} sertifikāti ar statusu '{}'", 
                filtered.size(), parakstits ? "parakstīts" : "neparakstīts");
        
        return filtered;
    }

    @Override
    public ArrayList<Sertifikati> filterByDateRange(LocalDate startDate, LocalDate endDate) throws Exception {
    	log.info("Filtrējam sertifikātus pēc datumu diapazona: {} līdz {}", startDate, endDate);
    	
    	ArrayList<Sertifikati> allSerts = (ArrayList<Sertifikati>) sertRepo.findAll();
        
        if (allSerts.isEmpty()) {
        	log.warn("Nav pieejams neviens sertifikāts datubāzē");
            throw new Exception("Nav pieejams neviens sertifikāts");
        }
        
        log.debug("Kopā datubāzē: {} sertifikāti", allSerts.size());
        
        ArrayList<Sertifikati> filtered = allSerts.stream()
                .filter(s -> {
                    LocalDate issueDate = s.getIzsniegtsDatums();
                    boolean afterStart = startDate == null || !issueDate.isBefore(startDate);
                    boolean beforeEnd = endDate == null || !issueDate.isAfter(endDate);
                    return afterStart && beforeEnd;
                })
                .collect(Collectors.toCollection(ArrayList::new));
        
        if (filtered.isEmpty()) {
        	log.warn("Nav atrasti sertifikāti norādītajā datumu diapazonā");
            throw new Exception("Nav atrasti sertifikāti norādītajā datumu diapazonā");
        }
        
        log.info("Filtrēšana pabeigta: atrasti {} sertifikāti datumu diapazonā", filtered.size());
        
        return filtered;
    }

    @Override
    public ArrayList<Sertifikati> filterByParticipant(long kdId) throws Exception {
    	log.info("Filtrējam sertifikātus pēc dalībnieka ID: {}", kdId);
        if (kdId <= 0) {
        	log.error("Nederīgs dalībnieka ID: {}", kdId);
            throw new Exception("Nederīgs dalībnieka ID");
        }
        
        ArrayList<Sertifikati> allSerts = (ArrayList<Sertifikati>) sertRepo.findAll();
        
        if (allSerts.isEmpty()) {
        	log.warn("Nav pieejams neviens sertifikāts datubāzē");
            throw new Exception("Nav pieejams neviens sertifikāts");
        }
        
        log.debug("Kopā datubāzē: {} sertifikāti", allSerts.size());
        
        ArrayList<Sertifikati> filtered = allSerts.stream()
                .filter(s -> s.getDalibnieks().getKdId() == kdId)
                .collect(Collectors.toCollection(ArrayList::new));
        
        if (filtered.isEmpty()) {
        	log.warn("Nav atrasti sertifikāti dalībniekam ar ID: {}", kdId);
            throw new Exception("Nav atrasti sertifikāti dalībniekam ar ID: " + kdId);
        }
        
        String dalibnieksNosaukums = filtered.get(0).getDalibnieks().getVards() + " " + 
                filtered.get(0).getDalibnieks().getUzvards();
        log.info("Filtrēšana pabeigta: atrasti {} sertifikāti dalībniekam: {}", 
        		filtered.size(), dalibnieksNosaukums);
        
        return filtered;
    }

    @Override
    public ArrayList<Sertifikati> filterByCourse(long kId) throws Exception {
    	log.info("Filtrējam sertifikātus pēc kursa ID: {}", kId);
        if (kId <= 0) {
        	log.error("Nederīgs kursa ID: {}", kId);
            throw new Exception("Nederīgs kursa ID");
        }
        
        ArrayList<Sertifikati> allSerts = (ArrayList<Sertifikati>) sertRepo.findAll();
        
        if (allSerts.isEmpty()) {
        	log.warn("Nav pieejams neviens sertifikāts datubāzē");
            throw new Exception("Nav pieejams neviens sertifikāts");
        }
        
        log.debug("Kopā datubāzē: {} sertifikāti", allSerts.size());
        
        ArrayList<Sertifikati> filtered = allSerts.stream()
                .filter(s -> s.getKursaDatums().getKurss().getKId() == kId)
                .collect(Collectors.toCollection(ArrayList::new));
        
        if (filtered.isEmpty()) {
        	log.warn("Nav atrasti sertifikāti kursam ar ID: {}", kId);
            throw new Exception("Nav atrasti sertifikāti kursam ar ID: " + kId);
        }
        
        String kursaNosaukums = filtered.get(0).getKursaDatums().getKurss().getNosaukums();
        log.info("Filtrēšana pabeigta: atrasti {} sertifikāti kursam: {}", 
            filtered.size(), kursaNosaukums);
        
        return filtered;
    }

    @Override
    public ArrayList<Sertifikati> combinedFilter(Boolean parakstits, LocalDate startDate, 
                                                LocalDate endDate, Long kdId, Long kId) throws Exception {
    	log.info("Kombinētā filtrēšana: parakstīts={}, datumi={} līdz {}, dalībnieksID={}, kursID={}", 
                parakstits, startDate, endDate, kdId, kId);
    	
        ArrayList<Sertifikati> allSerts = (ArrayList<Sertifikati>) sertRepo.findAll();
        
        if (allSerts.isEmpty()) {
        	log.warn("Nav pieejams neviens sertifikāts datubāzē");
            throw new Exception("Nav pieejams neviens sertifikāts");
        }
        
        log.debug("Kopā datubāzē: {} sertifikāti", allSerts.size());
        
        ArrayList<Sertifikati> filtered = allSerts.stream()
                .filter(s -> {
                    // Filter by parakstits status
                    if (parakstits != null && s.isParakstits() != parakstits) {
                        return false;
                    }
                    
                    // Filter by date range
                    LocalDate issueDate = s.getIzsniegtsDatums();
                    if (startDate != null && issueDate.isBefore(startDate)) {
                        return false;
                    }
                    if (endDate != null && issueDate.isAfter(endDate)) {
                        return false;
                    }
                    
                    // Filter by participant
                    if (kdId != null && s.getDalibnieks().getKdId() != kdId) {
                        return false;
                    }
                    
                    // Filter by course
                    if (kId != null && s.getKursaDatums().getKurss().getKId() != kId) {
                        return false;
                    }
                    
                    return true;
                })
                .collect(Collectors.toCollection(ArrayList::new));
        
        if (filtered.isEmpty()) {
            throw new Exception("Nav atrasti sertifikāti pēc norādītajiem kritērijiem");
        }
        log.info("Kombinētā filtrēšana pabeigta: atrasti {} sertifikāti", filtered.size());
        
        return filtered;
    }
}