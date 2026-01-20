package lv.venta.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import lv.venta.model.Sertifikati;

public interface ISertifikatiRepo extends CrudRepository<Sertifikati, Long>, 
                                          PagingAndSortingRepository<Sertifikati, Long> {
    
    public abstract Sertifikati findBySertId(long sertId);
    
    // Pagination metode (Spring Data automātiski implementē)
    Page<Sertifikati> findAll(Pageable pageable);
}