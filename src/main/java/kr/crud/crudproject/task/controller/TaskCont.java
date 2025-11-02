package kr.crud.crudproject.task.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TaskCont {

    @GetMapping("/user/tasks")
    public String userTaskPage() {
        return "user/tasks";
    }

    @GetMapping("/admin/tasks")
    public String adminTaskPage() {
        return "admin/tasks";
    }
}
