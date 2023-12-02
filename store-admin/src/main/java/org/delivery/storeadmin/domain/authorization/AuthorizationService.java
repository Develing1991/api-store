package org.delivery.storeadmin.domain.authorization;

import lombok.RequiredArgsConstructor;
import org.delivery.db.store.StoreRepository;
import org.delivery.db.store.enums.StoreStatus;
import org.delivery.storeadmin.domain.authorization.model.UserSession;
import org.delivery.storeadmin.domain.storeuser.service.StoreUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService {

    private final StoreUserService storeUserService;
    private final StoreRepository storeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var storeUserEntity = storeUserService.getStoreRegisteredUser(username);
        var storeEntity = Optional.ofNullable(storeRepository.findFirstByIdAndStatusOrderByIdDesc(storeUserEntity.get().getStoreId(), StoreStatus.REGISTERED));

        return storeUserEntity.map(storeUser ->{
            var userSession = UserSession.builder()
                    .userId(storeUser.getId())
                    .password(storeUser.getPassword())
                    .email(storeUser.getEmail())
                    .status(storeUser.getStatus())
                    .role(storeUser.getRole())
                    .registeredAt(storeUser.getRegisteredAt())
                    .unregisteredAt(storeUser.getUnregisteredAt())
                    .lastLoginAt(storeUser.getLastLoginAt())

                    .storeId(storeEntity.get().getId())
                    .storeName(storeEntity.get().getName())
                    .build();
            return userSession;

            /*
                return User.builder()
                    .username(storeUser.getEmail())
                    .password(storeUser.getPassword())
                    .roles(storeUser.getRole().toString())
                    .build();
            */
        }).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
