package ttlpta.ntq.todo_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class HomeController {

    @GetMapping("/hello")
    public String home() {
        return "Hello world";
    }
}
