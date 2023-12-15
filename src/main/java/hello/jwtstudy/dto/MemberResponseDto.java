package hello.jwtstudy.dto;

import hello.jwtstudy.entity.UserMan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private String username;

    public static MemberResponseDto of(UserMan userMan) {
        return new MemberResponseDto(userMan.getUsername());
    }
}
