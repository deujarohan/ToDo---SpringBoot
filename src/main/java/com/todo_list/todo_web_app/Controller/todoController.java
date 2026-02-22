package com.todo_list.todo_web_app.Controller;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;

import com.todo_list.todo_web_app.Model.ToDo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
public class todoController { 

    @GetMapping("/")
    @ResponseBody
    public String hello(){
        return "Hello World";
    }
    // Create List to Store Todo
    private List<ToDo> todoList = new ArrayList<>();

    // Create method to show page
    @GetMapping("/todo")
    public String showToDO(Model model){
        // Add the list of todos to the Model so the HTML page can access it
        // "createTodo" is the key name used in Thymeleaf (${createTodo})
        // todoList is the actual data (List of ToDo objects)
        // createTodo is a list created with this "todoList"
        //we are directly using todoList "createTodo" is just a name/key 
        // for the HTML to access the data — it does not create a new list.
        model.addAttribute("createTodo", todoList);
        return "index";
    }

//      Clicks Add → browser sends POST request to /addtodo
//      Spring controller method postMethodName(ToDo todo) runs once per click
//      Data is added → redirect to /todo → updated list shows
    // and once post method runs, the new object is cretaed of todo, and that 
    // object is added in this list, then we redirect to /todo
    @PostMapping("/addTodo")
    public String postMethodName(ToDo todo) {
        todoList.add(todo);
        // "redirect:" is a special prefix in Spring MVC
    //     It tells Spring:
    // “Don’t render a view template. Instead, send an HTTP redirect to the browser to this URL.”
        return "redirect:/todo";
    }
    

}
