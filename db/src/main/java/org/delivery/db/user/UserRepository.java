package org.delivery.db.user;

import org.delivery.db.user.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // select * from user where id = ? and status = ? order by id desc limit 1;
    Optional<UserEntity> findFirstByIdAndStatusOrderByIdDesc(Long id, UserStatus status);

    // select * from user where email =? and password = ? and status = ? order by id desc limit 1;
    Optional<UserEntity>findFirstByEmailAndPasswordAndStatusOrderByIdDesc(String email, String password, UserStatus status);
    // 얼티밋 버전은 쿼리 메소드 힌트도 줌
}
