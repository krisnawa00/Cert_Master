package lv.venta.repo;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.MyUser;

public interface IMyUserRepo extends CrudRepository<MyUser, Integer>{

	public abstract boolean existsByUsername(String username);

	public abstract MyUser findByUsername(String username);

	

}
