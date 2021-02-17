# b090041-spcdeinfoservice-client-spring

[![Java CI with Maven](https://github.com/jinahya/datagokr-api-b090041-spcdeinfoservice-client-spring/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/jinahya/datagokr-api-b090041-spcdeinfoservice-client-spring/actions)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jinahya_datagokr-api-b090041-spcdeinfoservice-client-spring&metric=alert_status)](https://sonarcloud.io/dashboard?id=jinahya_datagokr-api-b090041-spcdeinfoservice-client-spring)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/datagokr-api-b090041-spcdeinfoservice-client-spring)](https://search.maven.org/search?q=a:datagokr-api-b090041-spcdeinfoservice-client-spring)
[![javadoc](https://javadoc.io/badge2/com.github.jinahya/datagokr-api-b090041-spcdeinfoservice-client-spring/javadoc.svg)](https://javadoc.io/doc/com.github.jinahya/datagokr-api-b090041-spcdeinfoservice-client-spring)

A client library for accessing http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService.

See [특일 정보 (data.go.kr)](https://www.data.go.kr/data/15012690/openapi.do).

## Verify

Verify with your own service key assigned by the service provider.

```shell
$ mvn -Pfailsafe -DservcieKey=... clean verify
```

## Injection points

### Common

|Qualifier|Type|Notes|
|---------|----|-----------|
|`@SpcdeInfoServiceServiceKey`|`java.lang.String`|Provided by the service provider|

### For `RestTemplate`

|Qualifier|Type|Notes|
|---------|----|-----------|
|`@SpcdeInfoServiceRestTemplate`|[`RestTemplate`][RestTemplate]||
|`@SpcdeInfoServiceRestTemplateRootUri`|`j.l.String`|Optional|

### For `WebClient`

|Qualifier|Type|Notes|
|---------|----|-----------|
|`@SpcdeInfoServiceWebClient`|[`WebClient`][WebClient]||

## Usages

Expand the component-scanning path.

```java

@SpringBootApplication(
        scanBasePackageClasses = {
                com.github.jinahya.datagokr.....client.NoOp.class,
                MyApplication.class
        }
)
class MyApplication {

}
```

Provide the service key assigned by the service provider. Note that the service provider may give you a URL-encoded
value. You should use a URL-decoded value.

```java
@AbstractSpcdeInfoServiceClient.SpcdeInfoServiceServiceKey
@Bean
String spcdeInfoServiceServiceKey(){
    // The service key assigned by data.go.kr
    // Might be already URL-encoded
    // Use a URL-decoded value    
    // return "...%3D%3D"; (X)
    // return "...==";     (O)
}
```

### Using `RestTeamplate`

Provide an instance of `RestTemplate`.

```java
@SpcdeInfoServiceRestTemplate
@Bean
RestTemplate spcdeInfoServiceRestTemplate() {
    return new RestTemplateBuilder()
            ...
            .rootUri(AbstractSpcdeInfoServiceClient.BASE_URL_PRODUCTION)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
            .build();
}
```

Get `@Autowired` with an instance of `SpcdeInfoServiceClient` which is internally get autowired with
the `RestTemplate` instance.

```java
@Autowired
private SpcdeInfoServiceClient client;
```

### Using `WebClient`

Provide an instance of `WebClient`.

```java
@SpcdeInfoServiceWebClient
@Bean
WebClient spcdeInfoServiceWebClient() {
    return WebClient.builder()
            ...
            .baseUrl(AbstractSpcdeInfoServiceClient.BASE_URL_PRODUCTION)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
            .build();
}
```

Get `@Autowired` with an instance of `SpcdeInfoServiceReactiveClient` which is internally get autowired with the `WebClient` instance.

```java
@Autowired
private SpcdeInfoServiceReactiveClient client;
```


[RestTemplate]: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html
[WebClient]: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/reactive/function/client/WebClient.html