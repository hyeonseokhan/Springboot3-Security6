package com.toycode.study.security.application.port.in;

import com.toycode.study.security.domain.TokenInfo.Token;
import com.toycode.study.security.domain.User.Username;

public interface TokenParseUseCase {

    Username getUsernameFromToken(Token token);
}
