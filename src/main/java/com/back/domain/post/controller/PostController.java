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
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/write-form")
    @ResponseBody
    public String writeForm() {

        return getWriteForm("", "", "", "");
    }
    @AllArgsConstructor
    @Getter
    public static class PostWriteForm {
        @NotBlank
        @Size(min = 2, max = 10)
        private String title;

        @NotBlank
        @Size(min = 2, max = 100)
        private String content;
    }

    @PostMapping("/write")
    @ResponseBody
    public String write(@Valid PostWriteForm form) {
        Post post = postService.write(form.title, form.content);

        return "%d번 글이 작성되었습니다.".formatted(post.getId());
    }


    private String getWriteForm(String errorMessage, String title, String content, String errorFieldName) {
        return """
                 <div style="color:red">%s</div>
                <form method="POST" action="/posts/write">
                  <input type="text" name="title" value="%s" autoFocus>
                  <br>
                  <textarea name="content">%s</textarea>
                  <br>
                  <input type="submit" value="작성">
                </form>
                <script>
                    const errorFieldName = "%s";
                
                    if(errorFieldName.length > 0) {
                        const form = document.querySelector("form");
                        form[errorFieldName].focus();
                    }
                </script>
                """.formatted(errorMessage,title, content, errorFieldName);
    }
}