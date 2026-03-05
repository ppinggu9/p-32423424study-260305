package com.back.domain.post.controller;

import com.back.domain.post.entity.Post;
import com.back.domain.post.service.PostService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    public String writForm() {

        return getWriteForm("", "", "", "");
    }

    @PostMapping("/write")
    @ResponseBody
    public String write(
        @NotBlank @Size(min=2, max=10) String title,
        @NotBlank @Size(min=2, max=100) String content
    ) {

        if(title.isBlank()) return getWriteForm("제목을 입력해주세요.", title, content, "title");
        if(title.length() < 2) return getWriteForm("제목은 2글자 이상 적어주세요.", title, content, "title");
        if(title.length() > 10) return getWriteForm("제목은 10글자 이상 넘을 수 없습니다.", title, content, "title");
        if(content.isBlank()) return getWriteForm("내용을 입력해주세요.", title, content, "content");
        if(content.length() < 2) return getWriteForm("내용은 2글자 이상 적어주세요.", title, content, "content");
        if(content.length() > 100) return getWriteForm("내용은 100글자 이상 넘을 수 없습니다.", title, content, "content");

        Post post = postService.write(title, content);

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