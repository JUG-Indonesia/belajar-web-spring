package id.jug.spring.repositories.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.jug.spring.domain.Users;
import id.jug.spring.repositories.user.UserService;
import id.jug.spring.repositories.user.UserRepository;

/**
 * Created by galih.lasahido@gmail.com
 */
@Service
public class UserImpl implements UserService {
    @Autowired(required = true)
    private UserRepository repository;

    @Transactional
    @Override
    public void save(Users dto) {
        repository.saveAndFlush(dto);
    }

    @Transactional
    @Override
    public void delete(Users dto) {
        if (dto == null || dto.getId() == null) {
            return;
        }

        repository.delete(dto);
    }

    @Override
    public Users findById(Integer id) {
        if (id==null) {
            return null;
        }

        return repository.findOne(id);
    }

    @Override
    public Users findByUsername(String username) {
        if (username==null) {
            return null;
        }

        return repository.findUsersByUsernameIgnoreCase(username);
    }

    @Override
    public Users findByUsernameAndNotId(String username, Integer id) {
        if (username==null) {
            return null;
        }

        if (id==null) {
            return null;
        }

        return repository.findUsersByUsernameIgnoreCaseAndIdNot(username, id);
    }

    @Override
    public Page<Users> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Users> searchUsers(Pageable pageable, String mode, String value) {
        Page<Users> page = null;
        if(mode==null) {
            return null;
        }

        if(value==null) {
            return null;
        }

        switch (mode) {
            case"username":
                default:
                page = repository.getUsersByUsernameIgnoreCaseContaining(pageable,value);
                break;
            case"fullname":
                page = repository.getUsersByFullnameIgnoreCaseContaining(pageable,value);
                break;
        }

        return page;

    }

    @Transactional
    @Override
    public void updatePassword(String password, Integer id) {
        repository.updatePassword(password,id);
    }

    @Transactional
    @Override
    public void updateProfile(Users dto) {
        repository.updateProfile(dto.getFullname(), dto.getActive(), dto.getRoles(), dto.getId());
    }
    
    @Transactional
	@Override
	public void changepassword(Users data) {
		repository.updatePasswordUsername(data.getPassword(), data.getUsername());
	}
    
}