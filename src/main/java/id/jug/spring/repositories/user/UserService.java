package id.jug.spring.repositories.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import id.jug.spring.domain.Users;

/**
 * Created by galih.lasahido@gmail.com
 */
@Component
public interface UserService {
    public void save(Users dto);
    public void delete(Users dto);
    public Users findById(Integer id);
    public Users findByUsername(String username);
    public Users findByUsernameAndNotId(String username, Integer id);
    Page<Users> findAll(Pageable pageable);
    Page<Users> searchUsers(Pageable pageable, String mode, String value);
    public void updatePassword(String password, Integer id);
    public void updateProfile(Users dto);
	public void changepassword(Users data);
}