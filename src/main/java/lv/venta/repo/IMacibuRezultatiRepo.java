package lv.venta.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import lv.venta.model.MacibuRezultati;

public interface IMacibuRezultatiRepo extends CrudRepository<MacibuRezultati, Long>,
                                              PagingAndSortingRepository<MacibuRezultati, Long> {
    
    // Pagination metode (Spring Data automātiski implementē)
    Page<MacibuRezultati> findAll(Pageable pageable);
}