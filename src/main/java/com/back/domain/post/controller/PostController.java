package com.back.domain.post.controller;

import com.back.domain.post.entity.Post;
import com.back.domain.post.service.PostService;
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

        return getWriteForm("", "", "");
    }

    @PostMapping("/write")
    @ResponseBody
    public String write(@RequestParam String title, String content) {
        if (title.isBlank()) {
            return """
                    <div style="color:red">제목을 입력해주세요.</div>
                    %s
                    """.formatted(getWriteForm(title, content,"title"));
        }

        if (content.isBlank()) {
            return """
                    <div style="color:red">내용을 입력해주세요.</div>
                    %s
                    """.formatted(getWriteForm(title, content,"content"));
        }

        Post post = postService.write(title, content);

        return "%d번 글이 작성되었습니다.".formatted(post.getId());
    }


    private String getWriteForm(String title, String content, String errorFieldName) {
        return """
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
                """.formatted(title, content, errorFieldName);
    }
}