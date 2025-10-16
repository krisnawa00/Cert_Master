package lv.venta.service;

import java.util.ArrayList;

import lv.venta.model.Kurss;
import lv.venta.model.MacibuRezultati;

public interface IMacibuRezultatiService {

    ArrayList<MacibuRezultati> retrieveAllMacibuRezultati() throws Exception;

    MacibuRezultati retrieveMacibuRezultatiById(int id) throws Exception;

    void deleteMacibuRezultatiById(int id) throws Exception;


    void updateById(int id, Kurss kurss, boolean macibuRezultats) throws Exception;

    MacibuRezultati insertNewMacibuRezultats(long kId, boolean macibuRezultats) throws Exception;
}
