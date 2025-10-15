package lv.venta.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.venta.model.Kurss;
import lv.venta.model.MacibuRezultati;
import lv.venta.repo.IKurssRepo;
import lv.venta.repo.IMacibuRezultatiRepo;
import lv.venta.service.IMacibuRezultatiService;



@Service
public class MacibuRezultatiCrudService implements IMacibuRezultatiService {

    @Autowired
    private IMacibuRezultatiRepo macibuRezultatiRepo; 
    
    
    @Autowired
    private IKurssRepo kurssRepo;

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
    public void deleteMacibuRezultatiById(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID nevar būt negatīvs vai nulle");
        }
        if (!macibuRezultatiRepo.existsById((long) id)) {
            throw new Exception("Macību rezultāts ar ID " + id + " neeksistē");
        }
        macibuRezultatiRepo.deleteById((long) id);
    }

    @Override
    public MacibuRezultati insertNewMacibuRezultats(long kId, boolean macibuRezultats) throws Exception {
        if (kId <= 0) {
            throw new Exception("ID nevar būt negatīvs vai nulle");
        }
        
        if (!kurssRepo.existsById(kId)) {
            throw new Exception("Kurss ar ID " + kId + " neeksistē");
        }
        
        Kurss kurss = kurssRepo.findById(kId).get();
        MacibuRezultati newRezultats = new MacibuRezultati(kurss, macibuRezultats);
        
        return macibuRezultatiRepo.save(newRezultats);
    }
}
    
    

    


    
