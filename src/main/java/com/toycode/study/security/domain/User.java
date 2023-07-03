package com.toycode.study.security.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

@Getter
@AllArgsConstructor
public class User
//    implements UserDetails
{

    /**
     * 사용자의 유일한 식별 ID 값.
     */
    private final Username username;

    private String nickname;

    private String password;

    private Authority authority;

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }
//    @Override
//    public String getPassword() {
//        return this.password;
//    }
//    @Override
//    public String getUsername() {
//        return this.username.getValue();
//    }
//    // TODO 아래의 기능 구현 필요
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }

    /**
     * 사용자 패스워드가 포함되지 않은 생성자
     * @param username 사용자 명
     * @param nickname 사용자 닉네임
     * @param authority 사용자 권한
     * @return 패스워드가 포함되지 않은 사용자 객체
     */
    public static User withoutPassword(Username username, String nickname, Authority authority) {
        return new User(username, nickname, null, authority);
    }

    /**
     * 해당 객체(value object, VO)는 수정자(setter)가 없기 때문에 생성자를 통해 생성된 후 불변을 보장한다.
     */
    @Value
    public static class Username {

        String value;
    }
}
