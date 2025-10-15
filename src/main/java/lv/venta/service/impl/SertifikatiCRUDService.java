package lv.venta.service.impl;

import lv.venta.model.sertifikati;
import lv.venta.repo.ISertifikatiRepo;
import lv.venta.service.ISertifikatiService;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.venta.model.KursaDalibnieks;
import lv.venta.model.KursaDatumi;
import lv.venta.model.Sertifikati;
import lv.venta.repo.IEParakstaLogsRepo;
import lv.venta.repo.ISertRegTab;
import lv.venta.repo.ISertifikatiRepo;
import lv.venta.service.ISertifikatiService;

@Service
public class SertifikatiCRUDService implements ISertifikatiService {

    @Autowired
    private ISertifikatiRepo sertRepo;

    @Autowired
    private IEParakstaLogsRepo eparakstaLogsRepo;

    @Autowired
    private ISertRegTab sertRegTabRepo;


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


    public void updateById(int id,KursaDalibnieks dalibnieks, KursaDatumi kursaDatums,LocalDate izsniegtsDatums,boolean parakstits)throws Exception
    {
        if (dalibnieks == null || kursaDatums == null || izsniegtsDatums == null) {
        throw new Exception("Ievades parametri nav pareizi");
    }
        Sertifikati retrievedSert = retrieveSertifikatiById(id);
        retrievedSert.setDalibnieks(dalibnieks);
        retrievedSert.setKursaDatums(kursaDatums);
        retrievedSert.setIzsniegtsDatums(izsniegtsDatums);
        retrievedSert.setParakstits(parakstits);
        sertRepo.save(retrievedSert);


    }
    
    
    
    
    
    public void deleteSertifikatiById(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID nevar būt negatīvs vai nulle");
        }
        if (!sertRepo.existsById((long) id)) {
            throw new Exception("Sertifikāts ar ID " + id + " neeksistē");
        }
        Sertifikati certificate = sertRepo.findById((long) id).get();
        
        sertRegTabRepo.deleteAll(certificate.getSertifikatuRegistracijasTabula());
        eparakstaLogsRepo.deleteAll(certificate.getEparakstsLogs());
        
        sertRepo.deleteById((long) id);
    }








}