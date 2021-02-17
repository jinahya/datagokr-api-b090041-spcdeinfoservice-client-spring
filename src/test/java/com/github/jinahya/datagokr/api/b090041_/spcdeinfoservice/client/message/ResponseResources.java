package com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message;

import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.ApiDiscriminator.GET_24_DIVISIONS_INFO;
import static com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.ApiDiscriminator.GET_ANNIVERSARY_INFO;
import static com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.ApiDiscriminator.GET_HOLI_DE_INFO;
import static com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.ApiDiscriminator.GET_REST_DE_INFO;
import static com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message.ApiDiscriminator.GET_SUNDRY_DAY_INFO;
import static java.nio.file.Files.readAllLines;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

@Slf4j
public final class ResponseResources {

    // -----------------------------------------------------------------------------------------------------------------
    private static Response unmarshal(final URL url) throws JAXBException {
        requireNonNull(url, "url is null");
        final JAXBContext context = JAXBContext.newInstance(Response.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        return (Response) unmarshaller.unmarshal(url);
    }

    private static Response discriminate(final Response response, final String name) {
        if (name.contains("get24")) {
            response.getBody().getItems().forEach(i -> i.setApiDiscriminator(GET_24_DIVISIONS_INFO));
        } else if (name.contains("getAnni")) {
            response.getBody().getItems().forEach(i -> i.setApiDiscriminator(GET_ANNIVERSARY_INFO));
        } else if (name.contains("getHoli")) {
            response.getBody().getItems().forEach(i -> i.setApiDiscriminator(GET_HOLI_DE_INFO));
        } else if (name.contains("getRest")) {
            response.getBody().getItems().forEach(i -> i.setApiDiscriminator(GET_REST_DE_INFO));
        } else if (name.contains("getSund")) {
            response.getBody().getItems().forEach(i -> i.setApiDiscriminator(GET_SUNDRY_DAY_INFO));
        } else {
            throw new RuntimeException("unknown name: " + name);
        }
        return response;
    }

    private static List<Response> unmarshal(final Path path) throws IOException, JAXBException {
        final List<Response> responses = new ArrayList<>();
        final List<String> names = readAllLines(path).stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .filter(s -> !s.startsWith("#"))
                .collect(toList());
        for (String name : names) {
            final Response unmarshalled = unmarshal(ResponseResources.class.getResource(name));
            discriminate(unmarshalled, name);
            responses.add(unmarshalled);
        }
        return responses;
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static final List<Response> RESPONSES;

    static {
        final List<Response> responses;
        try {
            final Path path = Paths.get(ResponseResources.class.getResource("index.txt").toURI());
            responses = unmarshal(path);
        } catch (final Exception e) {
            e.printStackTrace();
            throw new InstantiationError(e.getMessage());
        }
        RESPONSES = unmodifiableList(responses);
    }

    public static Stream<Response> responses() {
        return RESPONSES.stream();
    }

    // --------------------------------------------------------------------------------------------------- index2021.txt
    private static final List<Response> RESPONSES2021;

    static {
        final List<Response> responses;
        try {
            final Path path = Paths.get(ResponseResources.class.getResource("index2021.txt").toURI());
            responses = unmarshal(path);
        } catch (final Exception e) {
            e.printStackTrace();
            throw new InstantiationError(e.getMessage());
        }
        RESPONSES2021 = unmodifiableList(responses);
    }

    public static Stream<Response> responses2021() {
        return RESPONSES2021.stream();
    }

    // -----------------------------------------------------------------------------------------------------------------
    private ResponseResources() {
        throw new AssertionError("instantiation is not allowed");
    }
}