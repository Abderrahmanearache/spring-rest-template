package net.armane.ar9am.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        initCross(httpServletRequest, httpServletResponse);


        String jwt = httpServletRequest.getHeader(Resources.AUTHORIZATION);

        if (jwt == null || !jwt.startsWith(Resources.TOKEN_PREFIX)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        Claims claims = Jwts.parser()
                .setSigningKey(Resources.JWT_SECRET)
                .parseClaimsJws(jwt.replace(Resources.TOKEN_PREFIX, ""))
                .getBody();
        String username = claims.getSubject();
        ArrayList<Map<String, String>> roles = ((ArrayList<Map<String, String>>) claims.get("roles"));

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        roles.forEach(e -> authorities.add(new SimpleGrantedAuthority(e.get("authority"))));

        UsernamePasswordAuthenticationToken springUser = new UsernamePasswordAuthenticationToken(
                username, null, authorities
        );
        SecurityContextHolder.getContext().setAuthentication(springUser);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }





    private void initCross(HttpServletRequest request, HttpServletResponse response) {

        response.addHeader("Access-Control-Allow-Origin",
                "*");
        response.addHeader("Access-Control-Allow-Headers",
                "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method," +
                        " Access-Control-RequestHeaders,authorization");
        response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin," +
                "Access-Control-Allow-Credentials, authorization");
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
        }

    }
}
