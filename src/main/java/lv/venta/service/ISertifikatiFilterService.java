package lv.venta.service;

import lv.venta.model.Sertifikati;
import java.time.LocalDate;
import java.util.ArrayList;

public interface ISertifikatiFilterService {
    
    ArrayList<Sertifikati> filterByParakstitStatus(boolean parakstits) throws Exception;
    
    ArrayList<Sertifikati> filterByDateRange(LocalDate startDate, LocalDate endDate) throws Exception;
    
    ArrayList<Sertifikati> filterByParticipant(long kdId) throws Exception;
    
    ArrayList<Sertifikati> filterByCourse(long kId) throws Exception;
    
    ArrayList<Sertifikati> combinedFilter(Boolean parakstits, LocalDate startDate, 
                                         LocalDate endDate, Long kdId, Long kId) throws Exception;
}