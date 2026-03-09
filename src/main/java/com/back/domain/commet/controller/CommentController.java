package com.back.domain.commet.controller;

import com.back.domain.post.entity.Post;
import com.back.domain.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    final PostService postService;

    @AllArgsConstructor
    @Getter
    public static class WriteRequestForm {
        @Size(min = 2, max = 100, message = "01-content-댓글내용은 2자 이상 100자 이하로 입력해주세요.")
        @NotBlank(message = "02-content-댓글내용은 필수입니다.")
        private String content;
    }


    @PostMapping("/write")
    @Transactional
    public String writeComment(@PathVariable int postId, @Valid WriteRequestForm from,
                               BindingResult bindingResult) {
        Post post = postService.findById(postId).get();

        if (bindingResult.hasErrors()) {
            return "detail";
        }
        post.addComment(from.content);

        return "redirect:/posts/%d".formatted(postId);
    }
}
