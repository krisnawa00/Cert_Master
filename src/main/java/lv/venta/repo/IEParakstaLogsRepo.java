package lv.venta.repo;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.EParakstaLogs;
import lv.venta.model.Sertifikati;

public interface IEParakstaLogsRepo extends CrudRepository<EParakstaLogs, Long> {

    void deleteBySertifikati(Sertifikati sertifikati);


}
