package com.alineumsoft.zenwk.security.auth.jwt;

import static com.alineumsoft.zenwk.security.common.constants.AuthConfigConstants.WEB_DETAILS;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.alineumsoft.zenwk.security.common.constants.CommonMessageConstants;
import com.alineumsoft.zenwk.security.enums.HttpMethodResourceEnum;
import com.alineumsoft.zenwk.security.enums.RoleEnum;
import com.alineumsoft.zenwk.security.enums.SecurityExceptionEnum;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Este filtro interceptara las peticiones y validara si el JWT es valido.
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class JwtAuthenticationFilter
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  /**
   * Utilidad manejadora del jwt
   */
  private final JwtProvider jwtProvider;

  /**
   * <p>
   * <b> Consructor </b>
   * </p>
   * 
   * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
   * @param jwtUtil
   */
  public JwtAuthenticationFilter(JwtProvider jwtProvider) {
    this.jwtProvider = jwtProvider;
  }

  /**
   * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
   * @param request
   * @param response
   * @param filterChain
   * @throws ServletException
   * @throws IOException
   * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(jakarta.servlet.http.HttpServletRequest,
   *      jakarta.servlet.http.HttpServletResponse, jakarta.servlet.FilterChain)
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String username = null;
    try {
      String token = jwtProvider.extractToken(request).orElse(null);
      // S el token es invaido continua con el proceso de autorizacion de la
      // sesion
      if (token == null) {
        filterChain.doFilter(request, response);
        return;
      }
      username = jwtProvider.extractAllClaims(token).getSubject();
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = jwtProvider.extractUserDetails(token).orElse(null);
        if (userDetails == null || !jwtProvider.validateToken(token, username)) {
          jwtProvider.sendErrorResponse(username, request, response,
              HttpServletResponse.SC_UNAUTHORIZED, SecurityExceptionEnum.FUNC_AUTH_TOKEN_INVALID);
          return;
        } else if (!validateRolUser(token, request)) {
          jwtProvider.sendErrorResponse(username, request, response,
              HttpServletResponse.SC_FORBIDDEN, SecurityExceptionEnum.FUNC_AUTH_URI_FORBIDEN);
          return;
        } else {
          authenticateUser(userDetails, request);
        }
      }
      filterChain.doFilter(request, response);
    } catch (JwtException e) {
      log.error(CommonMessageConstants.LOG_MSG_EXCEPTION, e);
      jwtProvider.sendErrorResponse(username, request, response,
          HttpServletResponse.SC_UNAUTHORIZED, e);
    }
  }

  /**
   * 
   * <p>
   * <b> CU001_Seguridad_Creacion_Usuario </b> Establece la autenticación del usuario en el contexto
   * de seguridad.
   * </p>
   * 
   * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
   * @param userDetails
   * @param request
   */
  private void authenticateUser(UserDetails userDetails, HttpServletRequest request) {
    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    setAuthenticationDetails(authToken, request);
    SecurityContextHolder.getContext().setAuthentication(authToken);
  }

  /**
   * 
   * <p>
   * <b> CU001_Seguridad_Creacion_Usuario </b> Verifica si el usuario con rol USER tiene acceso a la
   * URL solicitada.
   * </p>
   * </p>
   * 
   * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
   * @param token
   * @param request
   * @return
   */
  private boolean validateRolUser(String token, HttpServletRequest request) {
    List<String> roles = jwtProvider.extractAuthorities(token);
    String uri = request.getRequestURI();
    // Si el el rol user esta presente se inspeccionan que las uri correpondan a los
    // ids asociados a ese usuario
    if ((roles.contains((RoleEnum.USER.name())) || roles.contains((RoleEnum.NEW_USER.name())))
        && List
            .of(HttpMethodResourceEnum.USER_CREATE.getResource(),
                HttpMethodResourceEnum.PERSON_CREATE.getResource())
            .stream().anyMatch(uri::contains)) {
      return jwtProvider.extractUrlsAllowedRolUser(token).contains(uri);
    }
    return true;
  }

  /**
   * <p>
   * <b> CU001_Seguridad_Creacion_Usuario </b> Actualizacion de UsernamePasswordAuthenticationToken
   * con los permisos y metadatos
   * </p>
   * 
   * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
   * @param authentication
   * @param request
   */
  private void setAuthenticationDetails(UsernamePasswordAuthenticationToken authentication,
      HttpServletRequest request) {
    // Se usa cuando necesitas almacenar metadatos adicionales sobre la solicitud,
    // como: dirección IP, tipo de navegador, Información de sesión
    Map<String, Object> details =
        Map.of(WEB_DETAILS, new WebAuthenticationDetailsSource().buildDetails(request));
    authentication.setDetails(details);
  }

}
