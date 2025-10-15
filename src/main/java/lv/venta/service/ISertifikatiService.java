package lv.venta.service;

import lv.venta.model.sertifikati;
import java.util.ArrayList;

public interface ISertifikatiService {
    
    ArrayList<sertifikati> retrieveAllSertifikati() throws Exception;

    sertifikati retrieveSertifikatiById(long sertId) throws Exception;

    sertifikati insertNewSertifikats(long kdId, long kdatId, String izsniegtsDatums, boolean parakstits) throws Exception;
}