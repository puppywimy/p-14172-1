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
public class HomeController {
    @Operation(summary = "메인 페이지")
    @SneakyThrows
    @GetMapping(produces = TEXT_HTML_VALUE)
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

    @GetMapping(value = "/test/fetchPosts", produces = TEXT_HTML_VALUE)
    @Operation(summary = "fetchPosts 테스트")
    public String testFetchPosts() {
        return """
                <script>
                console.clear();
                
                fetch("http://localhost:8080/api/v1/posts")
                  .then(response => response.json())
                  .then(data => {
                    console.log(data);
                    console.log(data[0].title);
                  });
                </script>
                """;
    }
}