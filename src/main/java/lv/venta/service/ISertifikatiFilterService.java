package lv.venta.service;

import lv.venta.model.sertifikati;
import java.time.LocalDate;
import java.util.ArrayList;

public interface ISertifikatiFilterService {
    
    ArrayList<sertifikati> filterByParakstitStatus(boolean parakstits) throws Exception;
    
    ArrayList<sertifikati> filterByDateRange(LocalDate startDate, LocalDate endDate) throws Exception;
    
    ArrayList<sertifikati> filterByParticipant(long kdId) throws Exception;
    
    ArrayList<sertifikati> filterByCourse(long kId) throws Exception;
    
    ArrayList<sertifikati> combinedFilter(Boolean parakstits, LocalDate startDate, 
                                         LocalDate endDate, Long kdId, Long kId) throws Exception;
}