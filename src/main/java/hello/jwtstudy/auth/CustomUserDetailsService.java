package hello.jwtstudy.auth;

import hello.jwtstudy.entity.UserMan;
import hello.jwtstudy.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .map(this::creatUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username + " 찾을 수 없습니다."));

    }

    private UserDetails creatUserDetails(UserMan userMan) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userMan.getAuthority().toString());

        return new User(
                userMan.getUsername(),
                userMan.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}
