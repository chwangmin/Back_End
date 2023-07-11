package baby.lignin.template.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Example API", description = "Swagger Test  API")
@RestController
@RequestMapping("/")
public class ExampleController {
    @Operation(summary = "문자열 반환", description = "파라미터로 받은 문자열을 반환합니다.")
    @Parameter(name = "str", description = "반환할 문자열")
    @GetMapping("/text")
    public String returnText(@RequestParam String str) {
        return str;
    }

    @GetMapping("/example")
    public String example() {
        return "example API";
    }

    @Hidden
    @GetMapping("/hidden")
    public String hidden() {
        return "무시되는 API";
    }
}
