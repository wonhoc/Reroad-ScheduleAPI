package com.example.config;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


//스웨거 설정 : API 문서 관리 프레임워크
 


@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
   private static final Contact contact = new Contact("tuna", "https://cat-fish.atlassian.net/wiki/spaces/RER/overview", "mw51321681@gmail.com");
    private static final Set<String> sets = new HashSet<>(Arrays.asList("application/json"));


    /*
    @Bean
    public LinkDiscoverers discoverers() {
        List<LinkDiscoverer> plugins = new ArrayList<LinkDiscoverer>();
        plugins.add(new CollectionJsonLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.create(plugins));

    }
	
     
    
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("api")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/api.*"))
                .build()
                .apiInfo(apiInfo())
                .produces(sets)
                .consumes(sets);

    }

   
*/
    
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        String description = "스케줄조회 REST API 목록입니다.";
        return new ApiInfoBuilder()
                .title(" Reroad REST API List")
                .description(description)
                .version("1.0")
                .build();
    }
    
    
 
}//class end


