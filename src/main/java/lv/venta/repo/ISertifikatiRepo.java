package lv.venta.repo;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.sertifikati;

public interface ISertifikatiRepo extends CrudRepository<sertifikati, Long> {
    
    public abstract sertifikati findSertifikatiBySertId(long sertId);

}
