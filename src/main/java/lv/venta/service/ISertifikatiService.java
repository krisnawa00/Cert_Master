package lv.venta.service;

import lv.venta.model.Sertifikati;
import java.util.ArrayList;

public interface ISertifikatiService {
    
    ArrayList<Sertifikati> retrieveAllSertifikati() throws Exception;

    Sertifikati retrieveSertifikatiById(long sertId) throws Exception;
}