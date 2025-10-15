package lv.venta.service.impl;

import lv.venta.model.sertifikati;
import lv.venta.repo.ISertifikatiRepo;
import lv.venta.service.ISertifikatiFilterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class SertifikatiFilterService implements ISertifikatiFilterService {

    @Autowired
    private ISertifikatiRepo sertRepo;

    @Override
    public ArrayList<sertifikati> filterByParakstitStatus(boolean parakstits) throws Exception {
        ArrayList<sertifikati> allSerts = (ArrayList<sertifikati>) sertRepo.findAll();
        
        if (allSerts.isEmpty()) {
            throw new Exception("Nav pieejams neviens sertifikāts");
        }
        
        ArrayList<sertifikati> filtered = allSerts.stream()
                .filter(s -> s.isParakstits() == parakstits)
                .collect(Collectors.toCollection(ArrayList::new));
        
        if (filtered.isEmpty()) {
            throw new Exception("Nav atrasti sertifikāti ar statusu: " + (parakstits ? "parakstīts" : "neparakstīts"));
        }
        
        return filtered;
    }

    @Override
    public ArrayList<sertifikati> filterByDateRange(LocalDate startDate, LocalDate endDate) throws Exception {
        ArrayList<sertifikati> allSerts = (ArrayList<sertifikati>) sertRepo.findAll();
        
        if (allSerts.isEmpty()) {
            throw new Exception("Nav pieejams neviens sertifikāts");
        }
        
        ArrayList<sertifikati> filtered = allSerts.stream()
                .filter(s -> {
                    LocalDate issueDate = s.getIzsniegtsDatums();
                    boolean afterStart = startDate == null || !issueDate.isBefore(startDate);
                    boolean beforeEnd = endDate == null || !issueDate.isAfter(endDate);
                    return afterStart && beforeEnd;
                })
                .collect(Collectors.toCollection(ArrayList::new));
        
        if (filtered.isEmpty()) {
            throw new Exception("Nav atrasti sertifikāti norādītajā datumu diapazonā");
        }
        
        return filtered;
    }

    @Override
    public ArrayList<sertifikati> filterByParticipant(long kdId) throws Exception {
        if (kdId <= 0) {
            throw new Exception("Nederīgs dalībnieka ID");
        }
        
        ArrayList<sertifikati> allSerts = (ArrayList<sertifikati>) sertRepo.findAll();
        
        if (allSerts.isEmpty()) {
            throw new Exception("Nav pieejams neviens sertifikāts");
        }
        
        ArrayList<sertifikati> filtered = allSerts.stream()
                .filter(s -> s.getDalibnieks().getKdId() == kdId)
                .collect(Collectors.toCollection(ArrayList::new));
        
        if (filtered.isEmpty()) {
            throw new Exception("Nav atrasti sertifikāti dalībniekam ar ID: " + kdId);
        }
        
        return filtered;
    }

    @Override
    public ArrayList<sertifikati> filterByCourse(long kId) throws Exception {
        if (kId <= 0) {
            throw new Exception("Nederīgs kursa ID");
        }
        
        ArrayList<sertifikati> allSerts = (ArrayList<sertifikati>) sertRepo.findAll();
        
        if (allSerts.isEmpty()) {
            throw new Exception("Nav pieejams neviens sertifikāts");
        }
        
        ArrayList<sertifikati> filtered = allSerts.stream()
                .filter(s -> s.getKursaDatums().getKurss().getKId() == kId)
                .collect(Collectors.toCollection(ArrayList::new));
        
        if (filtered.isEmpty()) {
            throw new Exception("Nav atrasti sertifikāti kursam ar ID: " + kId);
        }
        
        return filtered;
    }

    @Override
    public ArrayList<sertifikati> combinedFilter(Boolean parakstits, LocalDate startDate, 
                                                LocalDate endDate, Long kdId, Long kId) throws Exception {
        ArrayList<sertifikati> allSerts = (ArrayList<sertifikati>) sertRepo.findAll();
        
        if (allSerts.isEmpty()) {
            throw new Exception("Nav pieejams neviens sertifikāts");
        }
        
        ArrayList<sertifikati> filtered = allSerts.stream()
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
        
        return filtered;
    }
}