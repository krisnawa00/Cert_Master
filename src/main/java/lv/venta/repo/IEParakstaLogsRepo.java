package lv.venta.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import lv.venta.model.EParakstaLogs;
import lv.venta.model.Sertifikati;

public interface IEParakstaLogsRepo extends CrudRepository<EParakstaLogs, Long>,
                                            PagingAndSortingRepository<EParakstaLogs, Long> {

    void deleteBySertifikati(Sertifikati sertifikati);
    
    // Pagination metode (Spring Data automātiski implementē)
    Page<EParakstaLogs> findAll(Pageable pageable);
}