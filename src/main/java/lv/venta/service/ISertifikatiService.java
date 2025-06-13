package lv.venta.service;

import lv.venta.model.sertifikati;
import java.util.ArrayList;

public interface ISertifikatiService {
    
    ArrayList<sertifikati> retrieveAllSertifikati() throws Exception;

    sertifikati retrieveSertifikatsById(long id) throws Exception;
}