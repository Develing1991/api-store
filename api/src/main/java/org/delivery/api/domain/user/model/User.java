package org.delivery.api.domain.user.model;

import lombok.*;
import org.delivery.db.user.enums.UserStatus;

import java.time.LocalDateTime;


// 실제 롬복의 동작은 빌드가 일어나가 getter가 생성이되고 나서야
// getter든 사용이 가능한테
// 코틀린에서 가져다 쓰다보니 컴파일 단계에서 에러가 나버림..
// 그래서 getter, setter만 직접 작성
//@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class User {

    private Long id;
    private String name;
    private String email;
    private String password;
    private UserStatus status;
    private String address;
    private LocalDateTime registeredAt;
    private LocalDateTime unregisteredAt;
    private LocalDateTime lastLoginAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    public LocalDateTime getUnregisteredAt() {
        return unregisteredAt;
    }

    public void setUnregisteredAt(LocalDateTime unregisteredAt) {
        this.unregisteredAt = unregisteredAt;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
}
