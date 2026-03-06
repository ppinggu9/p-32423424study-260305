package com.back.domain.post.controller;

import com.back.domain.post.entity.Post;
import com.back.domain.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/write-form")
    public String writeForm(@ModelAttribute("form") WriteRequestForm form){
        return "write";
    }

    @AllArgsConstructor
    @Getter
    public static class WriteRequestForm {
        @Size(min = 2, max = 10, message = "03-title-제목은 2자 이상 10자 이하로 입력해주세요.")
        @NotBlank(message = "01-title-제목은 필수입니다.")
        private String title;

        @Size(min = 2, max = 100, message = "04-content-내용은 2자 이상 100자 이하로 입력해주세요.")
        @NotBlank(message = "02-content-내용은 필수입니다.")
        private String content;
    }

    @PostMapping("/write")
    public String write(@Valid @ModelAttribute("form") WriteRequestForm form, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "write";
        }
        Post post = postService.write(form.title, form.content);

        // 템플릿 응답
        model.addAttribute("id", post.getId());
        return "writeDone";
    }
}