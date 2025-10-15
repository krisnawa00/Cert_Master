package lv.venta.service;

import java.util.ArrayList;

import lv.venta.model.Sertifikati;

public interface ISertifikatiService {
    
    ArrayList<Sertifikati> retrieveAllSertifikati() throws Exception;

    Sertifikati retrieveSertifikatiById(long sertId) throws Exception;
}