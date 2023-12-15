package hello.jwtstudy.controller;

import hello.jwtstudy.auth.AuthenticationService;
import hello.jwtstudy.dto.MemberRequestDto;
import hello.jwtstudy.dto.MemberResponseDto;
import hello.jwtstudy.dto.TokenDto;
import hello.jwtstudy.dto.TokenRequestDto;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;

    @PostMapping("/join")
    public MemberResponseDto join(@RequestBody MemberRequestDto memberRequestDto) {
        return service.join(memberRequestDto);
    }

    @PostMapping("/login")
    public TokenDto login(@RequestBody MemberRequestDto memberRequestDto) {
        return service.login(memberRequestDto);
    }

    //ROLE_USER 권한 확인
    @GetMapping("/user/check")
    public String check() {
        return "check";
    }

    @PostMapping("/reissue")
    public TokenDto reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return service.reissue(tokenRequestDto);
    }
}
