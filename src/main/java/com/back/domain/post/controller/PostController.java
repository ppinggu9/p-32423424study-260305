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

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/write-form")
    public String writeForm(@ModelAttribute("form") WriteRequestForm form) {
        return "write";
    }

    @AllArgsConstructor
    @Getter
    public static class WriteRequestForm {
        @Size(min = 2, max = 10, message = "3-제목은 2자 이상 10자 이하로 입력해주세요.")
        @NotBlank(message = "1-제목은 필수입니다.")
        private String title;

        @Size(min = 2, max = 100, message = "4-내용은 2자 이상 100자 이하로 입력해주세요.")
        @NotBlank(message = "2-내용은 필수입니다.")
        private String content;
    }

    @PostMapping("/write")
    public String write(@Valid @ModelAttribute("form") WriteRequestForm form, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map((fieldError) -> fieldError.getField() + "-" + fieldError.getDefaultMessage())
                    .map((message) -> {
                        String[] bits = message.split("-"); // [field, 1, errorMessage]
                        return "<!-- %s --> <li data-error-field=\"%s\">%s</li>".formatted(bits[1], bits[0], bits[2]);
                    })
                    .sorted()
                    .collect(Collectors.joining("\n"));

            // 템플릿 응답
            model.addAttribute("errorMessages", errorMessages);
            return "write";
        }

        Post post = postService.write(form.title, form.content);

        // 템플릿 응답
        model.addAttribute("id", post.getId());
        return "writeDone";
    }
}