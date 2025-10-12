package lv.venta.repo;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.Vertejums;

public interface IVertejumsRepo extends CrudRepository<Vertejums, Long> {

    public abstract Vertejums findByStudentKD_IDAndCourseK_ID(int dalibnieksId, int kurssId);

}
