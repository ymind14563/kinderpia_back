package sesac_3rd.sesac_3rd.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // req 에서 token 꺼내오기
            String token = parseBearerToken(request);
            log.info("JwtAuthenticationFilter is running...");

            // token 검사
            if (token != null && !token.equalsIgnoreCase("null")){
//                String userId = tokenProvider.validateAndGetUserId(token);
                String userIdString = tokenProvider.validateAndGetUserId(token);
                log.info("Authenticated user id: " + userIdString);

                // String 타입의 userId를 Long 타입으로 변환
                Long userId = Long.valueOf(userIdString);

                // 이전에 추출한 userId로 인증 객체 생성
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, AuthorityUtils.NO_AUTHORITIES);
                authentication.setDetails((new WebAuthenticationDetailsSource().buildDetails(request)));

                // 생성한 인증 객체를 Security Context 에 설정
                // - SecurityContextHolder 의 createEmptyContext 메서드를 이용해 SecurityContext 객체를 생성
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                // - 생성한 SecurityContext 에 인증된 정보인 authentication 를 넣고
                securityContext.setAuthentication(authentication);
                // - 다시 SecurityContextHolder 에 context 로 등록
                SecurityContextHolder.setContext(securityContext);
            }
        } catch (Exception e){
            logger.error("Could not set user authentication in security context",e);
        }

        // 필터 체인 계속 진행
        filterChain.doFilter(request, response);

    }

//    // req.headers 에서 Bearer Token 을 꺼내오는 메서드
//    private String parseBearerToken(HttpServletRequest request){
//        String bearerToken = request.getHeader("Cookie");
//
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("jwt=")){
//            System.out.println("bearer>>>>>>>>" + bearerToken.substring(4));
//            return bearerToken.substring(4);
//            // req.header jwt 토큰이 다음과 같이 들어있으므로 문자열 슬라이싱 진행하여 반환
//            // Authentication: "Bearer asdfasdf.asdfasdf.asdfasdf"
//        }
//
//        return null;
//    }

    // req.headers 에서 Bearer Token 을 꺼내오는 메서드 (`jwt=` 를 명확히 추출)
    private String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Cookie");

        // "Cookie" 헤더에 "jwt="라는 쿠키가 있는지 확인
        if (StringUtils.hasText(bearerToken) && bearerToken.contains("jwt=")) {
            // 여러 개의 쿠키가 있을 수 있으므로 ";"로 분리하고 "jwt="로 시작하는 쿠키 찾기
            for (String cookie : bearerToken.split(";")) {
                cookie = cookie.trim();
                if (cookie.startsWith("jwt=")) {
                    System.out.println("bearer>>>>>>>>" + cookie.substring(4));
                    return cookie.substring(4); // "jwt=" 이후의 토큰 값 반환
                }
            }
        }

        return null; // "jwt=" 쿠키가 없을 경우 null 반환
    }
}
