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
    public sertifikati retrieveSertifikatiById(long sertId) throws Exception {
        if (sertId <= 0) {
            throw new Exception("ID nevar būt negatīvs vai nulle");
        }
        if (!sertRepo.existsById(sertId)) {
            throw new Exception("Sertifikāts ar ID " + sertId + " neeksistē");
        }
        return sertRepo.findById(sertId).get();
    }
}