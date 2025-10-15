package lv.venta.repo;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.Sertifikati;

public interface ISertifikatiRepo extends CrudRepository<Sertifikati, Long> {
    
    public abstract Sertifikati findBySertId(long sertId);

}
