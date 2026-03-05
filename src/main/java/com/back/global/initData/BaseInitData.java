package com.back.global.initData;

import com.back.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    private final PostService postService;
    @Autowired
    @Lazy
    private BaseInitData self;

    @Bean
    ApplicationRunner initDataRunner() {
        return args -> {
            work1();
        };
    }


    public void work1() {
        if(postService.count() > 0) {
            return;
        }
        postService.write("제목1", "내용1");
        postService.write("제목2", "내용2");
        postService.write("제목3", "내용3");
    }
}