package lv.venta.service;

import java.time.LocalDate;
import java.util.ArrayList;

import lv.venta.model.KursaDalibnieks;
import lv.venta.model.KursaDatumi;
import lv.venta.model.sertifikati;

public interface ISertifikatiService {
    
    ArrayList<sertifikati> retrieveAllSertifikati() throws Exception;
    
    void create(KursaDalibnieks dalibnieks, KursaDatumi kursaDatums, LocalDate izsniegtsDatums, boolean parakstits) throws Exception;

	sertifikati retrieveSertifikatsById(long sertId) throws Exception;
}