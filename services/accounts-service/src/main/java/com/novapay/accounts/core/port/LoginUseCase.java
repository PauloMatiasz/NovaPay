package com.novapay.accounts.core.port;

import com.novapay.accounts.core.usecase.auth.LoginResult;

public interface LoginUseCase {
    LoginResult login(LoginCommand command);
}
