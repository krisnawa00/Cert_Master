package lv.venta.service;

import lv.venta.model.Sertifikati;
import java.util.ArrayList;

public interface ISertifikatiService {
    
    ArrayList<Sertifikati> retrieveAllSertifikati() throws Exception;

    Sertifikati retrieveSertifikatiById(long sertId) throws Exception;

    Sertifikati insertNewSertifikats(long kdId, long kdatId, String izsniegtsDatums, boolean parakstits) throws Exception;
}