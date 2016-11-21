package id.jug.spring.repositories.user;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import id.jug.spring.domain.Users;


/**
 * Created by galih.lasahido@gmail.com
 */
@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    public Users findUsersByUsernameIgnoreCase(String username);
    public Page<Users> getUsersByUsernameIgnoreCaseContaining(Pageable pageable, String username);
    public Page<Users> getUsersByFullnameIgnoreCaseContaining(Pageable pageable, String fullname);
    public Users findUsersByUsernameIgnoreCaseAndIdNot(String username, Integer id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Users u SET u.password = ?1 WHERE u.id = ?2 ")
    public void updatePassword(String password, Integer id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Users u SET u.password = ?1 WHERE u.username = ?2 ")
    public void updatePasswordUsername(String password, String username);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Users u SET u.fullname = ?1, u.active = ?2, u.roles = ?3 WHERE u.id = ?4")
    public void updateProfile(String fullname, Boolean active, String roles, Integer id);
}