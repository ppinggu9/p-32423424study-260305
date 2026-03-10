package com.back.domain.commet.controller;

import com.back.domain.commet.entity.Comment;
import com.back.domain.post.entity.Post;
import com.back.domain.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    private final PostService postService;

    record WriteRequestForm(
            @Size(min = 2, max = 100, message = "01-content-댓글내용은 2자 이상 100자 이하로 입력해주세요.")
            @NotBlank(message = "02-content-댓글내용은 필수입니다.")
            String content
    ) {}

    record ModifyRequestForm(
            @Size(min = 2, max = 100, message = "01-content-댓글내용은 2자 이상 100자 이하로 입력해주세요.")
            @NotBlank(message = "02-content-댓글내용은 필수입니다.")
            String content
    ) {}

    @PostMapping("/write")
    @Transactional
    public String writeComment(@PathVariable int postId, @Valid WriteRequestForm from) {
        Post post = postService.findById(postId).get();
        post.addComment(from.content);
        return "redirect:/posts/%d".formatted(postId);
    }

    @GetMapping("/{commentId}/modify")
    public String modify(@PathVariable int postId, @PathVariable int commentId, Model model) {
        Post post = postService.findById(postId).get();
        Comment comment = post.findCommentById(commentId).get();
        model.addAttribute("comment", comment);
        model.addAttribute("post", post);

        return "comment_modify";
    }

    @PutMapping("/{commentId}/modify")
    @Transactional
    public String modify(@PathVariable int postId, @PathVariable int commentId, @Valid ModifyRequestForm form) {
        Post post = postService.findById(postId).get();
        Comment comment = post.findCommentById(commentId).get();
        comment.update(form.content);
        return "redirect:/posts/%d".formatted(post.getId());
    }

    @DeleteMapping("/{commentId}")
    @Transactional
    public String delete(@PathVariable int postId, @PathVariable int commentId) {
        Post post = postService.findById(postId).get();
        post.deleteComment(commentId);

        return "redirect:/posts/%d".formatted(post.getId());
    }
}
