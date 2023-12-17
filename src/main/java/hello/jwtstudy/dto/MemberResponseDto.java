package hello.jwtstudy.dto;

import hello.jwtstudy.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private String username;

    public static MemberResponseDto of(User user) {
        return new MemberResponseDto(user.getUsername());
    }
}
