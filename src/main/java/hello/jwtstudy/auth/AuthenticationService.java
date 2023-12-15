package hello.jwtstudy.auth;

import hello.jwtstudy.dto.MemberRequestDto;
import hello.jwtstudy.dto.MemberResponseDto;
import hello.jwtstudy.dto.TokenDto;
import hello.jwtstudy.dto.TokenRequestDto;
import hello.jwtstudy.entity.Authority;
import hello.jwtstudy.entity.RefreshToken;
import hello.jwtstudy.entity.UserMan;
import hello.jwtstudy.jwt.TokenProvider;
import hello.jwtstudy.repository.MemberRepository;
import hello.jwtstudy.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public MemberResponseDto join(MemberRequestDto memberRequestDto) {
        if (memberRepository.existsByUsername(memberRequestDto.getUsername())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        UserMan user = UserMan.builder()
                .username(memberRequestDto.getUsername())
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .authority(Authority.ROLE_USER)
                .build();
        UserMan savedUser = memberRepository.save(user);

        return MemberResponseDto.of(savedUser);
    }

    public TokenDto login(MemberRequestDto memberRequestDto) {

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(memberRequestDto.getUsername(), memberRequestDto.getPassword());
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authenticate);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authenticate.getName())
                .value(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("로그아웃된 사용자 입니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃된 사용자입니다."));

        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            System.out.println("refreshToken = " + refreshToken);
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }


}
