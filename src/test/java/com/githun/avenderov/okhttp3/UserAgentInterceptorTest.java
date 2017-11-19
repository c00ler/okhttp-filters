package com.githun.avenderov.okhttp3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.avenderov.api.TracksApi;
import com.github.avenderov.model.Track;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

public class UserAgentInterceptorTest {

    private static final String APPLICATION_JSON = "application/json";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

    private ObjectMapper mapper;

    private TracksApi underTest;

    @Before
    public void beforeEach() {
        mapper = new ObjectMapper();

        final OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new UserAgentInterceptor("project"))
                .connectTimeout(100, TimeUnit.MILLISECONDS)
                .readTimeout(100, TimeUnit.MILLISECONDS)
                .writeTimeout(100, TimeUnit.MILLISECONDS)
                .build();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(String.format("http://localhost:%d/", wireMockRule.port()))
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build();

        underTest = retrofit.create(TracksApi.class);
    }

    @Test
    public void shouldGetTrackById() throws Exception {
        final String id = UUID.randomUUID().toString();
        final Track track = new Track("The Unforgiven", "Metallica");

        stubFor(get(urlEqualTo("/tracks/" + id))
                .withHeader("User-Agent", containing("project (okhttp"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON)
                        .withBody(mapper.writeValueAsString(track))));

        final Response<Track> response = underTest.getById(id).execute();

        assertThat(response.code()).isEqualTo(200);
        assertThat(response.body()).isEqualTo(track);
    }

}
