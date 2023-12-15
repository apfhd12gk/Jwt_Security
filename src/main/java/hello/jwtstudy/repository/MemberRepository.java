package hello.jwtstudy.repository;

import hello.jwtstudy.entity.UserMan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<UserMan,Long> {
    Optional<UserMan> findByUsername(String username);

    boolean existsByUsername(String username);
}
