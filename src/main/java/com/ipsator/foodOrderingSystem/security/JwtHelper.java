/**
 * Helper class for working with JSON Web Tokens (JWT) in the application.
 */
package com.ipsator.foodOrderingSystem.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Utility class for creating, parsing, and validating JSON Web Tokens (JWT).
 */
@Component
public class JwtHelper {

  /**
   * The validity duration of JWT tokens in seconds.
   */
  public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;


  /**
   * The secret key used for signing and verifying JWT tokens.
   */
  private String secret = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";

  /**
   * Extracts the username from a JWT token.
   *
   * @param token The JWT token to extract the username from.
   * @return The username extracted from the token.
   */
  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  /**
   * Retrieves the expiration time from a JWT token.
   *
   * @param token The JWT token to retrieve the expiration date from.
   * @return The expiration date extracted from the token.
   */
  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  /**
   * Extracts a specific claim from a JWT token.
   *
   * @param token          The JWT token to extract the claim from.
   * @param claimsResolver A function to resolve the desired claim from the token's claims.
   * @param <T>            The type of the claim to extract.
   * @return The claim extracted from the token.
   */
  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Retrieves all claims from a JWT token.
   *
   * @param token The JWT token to retrieve all claims from.
   * @return All claims extracted from the token.
   */
  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  /**
   * Checks if a JWT token has expired.
   *
   * @param token The JWT token to check for expiration.
   * @return True if the token has expired, false otherwise.
   */
  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }


  /**
   * Generates a JWT token for a user.
   *
   * @param userDetails The UserDetails representing the user.
   * @return The generated JWT token.
   */
  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return doGenerateToken(claims, userDetails.getUsername());
  }

  /**
   * Creates a JWT token based on claims and subject.
   *
   * @param claims  The claims to include in the token.
   * @param subject The subject (usually the username) for the token.
   * @return The generated JWT token.
   */
  private String doGenerateToken(Map<String, Object> claims, String subject) {

    return Jwts.builder().setClaims(claims).setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
        .signWith(SignatureAlgorithm.HS512, secret).compact();
  }

  /**
   * Validates a JWT token for a user.
   *
   * @param token       The JWT token to validate.
   * @param userDetails The UserDetails representing the user.
   * @return True if the token is valid for the user, false otherwise.
   */
  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = getUsernameFromToken(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

}
