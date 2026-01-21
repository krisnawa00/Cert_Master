package lv.venta.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.venta.model.Sertifikati;
import lv.venta.repo.ISertifikatiRepo;
import lv.venta.service.ISertifikatiStatistikaService;

@Service
public class SertifikatiStatistikaServiceImpl implements ISertifikatiStatistikaService {

    @Autowired
    private ISertifikatiRepo sertRepo;

    @Override
    public Map<String, Object> getGeneralStatistics() throws Exception {
        ArrayList<Sertifikati> allSerts = (ArrayList<Sertifikati>) sertRepo.findAll();
        
        if (allSerts.isEmpty()) {
            throw new Exception("Nav pieejami sertifikāti statistikai");
        }

        Map<String, Object> stats = new HashMap<>();
        
        // Kopējais skaits
        stats.put("total", allSerts.size());
        
        // Parakstītie
        long parakstiti = allSerts.stream()
            .filter(Sertifikati::isParakstits)
            .count();
        stats.put("parakstiti", parakstiti);
        
        // Neparakstītie
        stats.put("neparakstiti", allSerts.size() - parakstiti);
        
        // Procentuālā daļa parakstīto
        double percent = (parakstiti * 100.0) / allSerts.size();
        stats.put("parakstitiProcenti", String.format("%.1f%%", percent));
        
        // Šomēnes izsniegti
        LocalDate now = LocalDate.now();
        long soMenes = allSerts.stream()
            .filter(s -> s.getIzsniegtsDatums().getMonth() == now.getMonth() 
                      && s.getIzsniegtsDatums().getYear() == now.getYear())
            .count();
        stats.put("soMenes", soMenes);
        
        // Šogad izsniegti
        long soGad = allSerts.stream()
            .filter(s -> s.getIzsniegtsDatums().getYear() == now.getYear())
            .count();
        stats.put("soGad", soGad);
        
        return stats;
    }

    @Override
    public Map<String, Long> getStatisticsByMonth() throws Exception {
        ArrayList<Sertifikati> allSerts = (ArrayList<Sertifikati>) sertRepo.findAll();
        
        if (allSerts.isEmpty()) {
            throw new Exception("Nav pieejami sertifikāti statistikai");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        
        return allSerts.stream()
            .collect(Collectors.groupingBy(
                s -> s.getIzsniegtsDatums().format(formatter),
                Collectors.counting()
            ))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByKey().reversed())
            .limit(12) // Pēdējie 12 mēneši
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                HashMap::new
            ));
    }

    @Override
    public Map<String, Long> getStatisticsByCourse() throws Exception {
        ArrayList<Sertifikati> allSerts = (ArrayList<Sertifikati>) sertRepo.findAll();
        
        if (allSerts.isEmpty()) {
            throw new Exception("Nav pieejami sertifikāti statistikai");
        }

        return allSerts.stream()
            .collect(Collectors.groupingBy(
                s -> s.getKursaDatums().getKurss().getNosaukums(),
                Collectors.counting()
            ))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                HashMap::new
            ));
    }

    @Override
    public Map<String, Long> getStatisticsByCity() throws Exception {
        ArrayList<Sertifikati> allSerts = (ArrayList<Sertifikati>) sertRepo.findAll();
        
        if (allSerts.isEmpty()) {
            throw new Exception("Nav pieejami sertifikāti statistikai");
        }

        return allSerts.stream()
            .collect(Collectors.groupingBy(
                s -> s.getDalibnieks().getPilseta().toString(),
                Collectors.counting()
            ))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(10) // Top 10 pilsētas
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                HashMap::new
            ));
    }
}