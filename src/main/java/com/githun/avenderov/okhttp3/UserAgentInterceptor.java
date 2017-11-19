package com.githun.avenderov.okhttp3;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Version;

import java.io.IOException;

public final class UserAgentInterceptor implements Interceptor {

    private final String userAgent;

    public UserAgentInterceptor(final String project) {
        if (project == null || project.isEmpty()) {
            throw new IllegalArgumentException("project must not be empty");
        }

        this.userAgent = String.format("%s (%s)", project, Version.userAgent());
    }

    @Override
    public Response intercept(final Chain chain) throws IOException {
        final Request originalRequest = chain.request();

        return chain.proceed(originalRequest.newBuilder()
                .header("User-Agent", userAgent)
                .build());
    }

}
