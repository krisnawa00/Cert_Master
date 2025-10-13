package lv.venta.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.venta.model.Kurss;
import lv.venta.model.MacibuRezultati;
import lv.venta.repo.IMacibuRezultatiRepo;
import lv.venta.service.IMacibuRezultatiService;



@Service
public class MacibuRezultatiCrudService implements IMacibuRezultatiService {

    @Autowired
    private IMacibuRezultatiRepo macibuRezultatiRepo; 


    //retrieve all
    @Override
    public ArrayList<MacibuRezultati> retrieveAllMacibuRezultati() throws Exception {
        if (macibuRezultatiRepo.count() == 0) {
            throw new Exception("Nav pieejami neviens macību rezultāts");
        }
        return (ArrayList<MacibuRezultati>) macibuRezultatiRepo.findAll();
    }

    @Override
    public MacibuRezultati retrieveMacibuRezultatiById(int id) throws Exception {

        if (id <= 0) {
            throw new Exception("ID nevar būt negatīvs vai nulle");
        }
        if (!macibuRezultatiRepo.existsById((long) id)) {
            throw new Exception("Macību rezultāts ar ID " + id + " neeksistē");
        }
        return macibuRezultatiRepo.findById((long) id).get();
    }
    
    @Override
    public void updateById(int id, Kurss kurss, boolean macibuRezultats) throws Exception {
    if (kurss == null) {
        throw new Exception("Ievades parametri nav pareizi");
    }
    MacibuRezultati retrievedMacR = retrieveMacibuRezultatiById(id);
    retrievedMacR.setKurss(kurss);
    retrievedMacR.setMacibuRezultats(macibuRezultats);
    macibuRezultatiRepo.save(retrievedMacR);
}


    
    @Override
    public void deleteMacibuRezultatiById(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID nevar būt negatīvs vai nulle");
        }
        if (!macibuRezultatiRepo.existsById((long) id)) {
            throw new Exception("Macību rezultāts ar ID " + id + " neeksistē");
        }
        macibuRezultatiRepo.deleteById((long) id);
    }

    
    
    

    


    
}
