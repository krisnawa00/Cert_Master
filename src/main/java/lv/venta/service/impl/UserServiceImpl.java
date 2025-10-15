package lv.venta.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.venta.model.MyUser;
import lv.venta.repo.IMyUserRepo;
import lv.venta.service.IUserService;

@Service
public class UserServiceImpl implements IUserService{
	@Autowired
	private IMyUserRepo userRepo;
	@Override
	public int getUserIdFromUsername(String username) {
		MyUser user = userRepo.findByUsername(username);
		return user.getUId();
	}
	
	
}
