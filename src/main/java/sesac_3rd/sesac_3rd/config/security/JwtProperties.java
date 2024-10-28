package sesac_3rd.sesac_3rd.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("jwt")
public class JwtProperties {
    private String issuer;   // jwt.issuer
    private String secretkey;  // jwt.secret_key
}
