package lv.venta.service.impl;

import lv.venta.model.sertifikati;
import lv.venta.repo.ISertifikatiRepo;
import lv.venta.service.ISertifikatiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SertifikatiCRUDService implements ISertifikatiService {

    @Autowired
    private ISertifikatiRepo sertRepo;

    @Override
    public ArrayList<sertifikati> retrieveAllSertifikati() throws Exception {
        if (sertRepo.count() == 0) {
            throw new Exception("Nav pieejams neviens sertifikāts");
        }
        return (ArrayList<sertifikati>) sertRepo.findAll();
    }

    @Override
    public sertifikati retrieveSertifikatsById(long id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID nevar būt negatīvs vai nulle");
        }
        if (!sertRepo.existsById(id)) {
            throw new Exception("Sertifikāts ar ID " + id + " neeksistē");
        }
        return sertRepo.findById(id).get();
    }
}