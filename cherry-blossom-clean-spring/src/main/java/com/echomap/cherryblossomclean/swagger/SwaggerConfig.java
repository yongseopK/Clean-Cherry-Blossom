package com.echomap.cherryblossomclean.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(title = "API 문서", version = "0.0.1", description = "깨끗한 꽃놀이 API 문서"),
    tags = {
      @Tag(name = "회원 관리", description = "회원관리 기능 API"),
      @Tag(name = "제보 관리", description = "제보관리 기능 API"),
      @Tag(name = "지도 관리", description = "지도관리 기능 API")
    })
@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .components(new Components())
        .info(
            new io.swagger.v3.oas.models.info.Info()
                .title("API 문서")
                .description("프로젝트 API 문서")
                .version("1.0")
                .license(
                    new License().name("MIT License").url("https://opensource.org/licenses/MIT")));
  }
}
