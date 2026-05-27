package com.back.domain.home.home.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;

import static java.net.InetAddress.getLocalHost;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

@RestController
@Tag(name = "HomeController", description = "홈 컨트롤러")
@RequestMapping(produces = TEXT_HTML_VALUE)
public class HomeController {
    @Operation(summary = "메인 페이지")
    @SneakyThrows
    @GetMapping
    public String main() {
        InetAddress localHost = getLocalHost();

        return """
                <h1>API 서버</h1>
                <p>Host Name: %s</p>
                <p>Host Address: %s</p>
                <div>
                    <a href="/swagger-ui/index.html">API 문서로 이동</a>
                </div>
                """.formatted(localHost.getHostName(), localHost.getHostAddress());
    }
}