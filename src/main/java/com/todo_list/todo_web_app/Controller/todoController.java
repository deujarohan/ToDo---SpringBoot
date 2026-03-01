package com.todo_list.todo_web_app.Controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.todo_list.todo_web_app.Model.ToDo;
import com.todo_list.todo_web_app.Service.todoService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;





@Controller
public class todoController { 

    @Autowired
    private todoService todoService;


    @GetMapping("/")
    public String landingPage(){
        return "index";
        
    }

    
    List<ToDo> todoList;

    // Create method to show page
    @GetMapping("/todo")
    public String showToDO(Model model){
        // Add the list of todos to the Model so the HTML page can access it
        // "createTodo" is the key name used in Thymeleaf (${createTodo})
        // todoList is the actual data (List of ToDo objects)
        // createTodo is a list created with this "todoList"
        //we are directly using todoList "createTodo" is just a name/key 
        // for the HTML to access the data — it does not create a new list.
        // --------------------------------------------------------------
        // Create List to Store Todo
        todoList = todoService.getAllTodos();
        model.addAttribute("createTodo", todoList);
        // --------------------------------------------------------------
        // todoService.getAllTodos();
        // return "list";   
        return "list";
    }

//      Clicks Add → browser sends POST request to /addtodo
//      Spring controller method postMethodName(ToDo todo) runs once per click
//      Data is added → redirect to /todo → updated list shows
    // and once post method runs, the new object is cretaed of todo, and that 
    // object is added in this list, then we redirect to /todo
    @PostMapping("/addTodo")
    public String postMethodName(ToDo todo) {
        // --------------------------------------------------------------
        // todoList.add(todo);
        // --------------------------------------------------------------
        todoService.saveTodo(todo);
        // "redirect:" is a special prefix in Spring MVC
    //     It tells Spring:
    // “Don’t render a view template. Instead, send an HTTP redirect to the browser to this URL.”
        return "redirect:/todo";
    }

    @GetMapping("/todo/{id}")
    public ToDo getById(@PathVariable ObjectId id){
        return todoService.findTodoById(id)
                      .orElseThrow(() -> new ResponseStatusException(
                           HttpStatus.NOT_FOUND, "ID not found"
                      ));
    }

    // GET — show confirmation page
    @GetMapping("/todo/delete/{id}")
    public String getDeleteConfirm(@PathVariable String id, Model model) {
        ObjectId objId = new ObjectId(id);
        ToDo todo = todoService.findTodoById(objId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found"));
        model.addAttribute("todo", todo);
        return "delete"; // renders delete.html
    }
    
    //Delete method
    @PostMapping("/todo/delete/{id}")
    public String deleteId(@PathVariable ObjectId id){
        // todoService.deleteTodo(id);
        // return "redirect:/todo";
        try {
            todoService.deleteTodo(id);
            System.out.println("Todo deleted successfully");
            
            String redirectUrl = "redirect:/todo";
            System.out.println("Redirecting to: " + redirectUrl);
            
            return redirectUrl;
        } catch (Exception e) {
            System.out.println("ERROR during deletion: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/todo?error=true";
        }
    }

    // GET — show the pre-filled edit form
    @GetMapping("/todo/update/{id}")
    public String getUpdateForm(@PathVariable String id, Model model) {
        ObjectId objId;
        try {
            objId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Id format");
        }

        ToDo todo = todoService.findTodoById(objId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found"));

        model.addAttribute("todo", todo);
        return "update"; // renders update.html
    }
    
    // POST — receive the form submission and save
    @PostMapping("/todo/update/{id}")
    public String putMethodName(@PathVariable String id, @RequestBody ToDo oldJson) {
        ObjectId objId;
        try {
            objId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Id format");
        }
        ToDo newObj = todoService.findTodoById(objId).orElseThrow(() -> new RuntimeException("Id not found"));
        if (oldJson.getTitle() != null && !oldJson.getTitle().isEmpty()) {
            newObj.setTitle(oldJson.getTitle());
        }
        if (oldJson.getDescription() != null) {
            newObj.setDescription(oldJson.getDescription());
        }
    
        newObj.setCompleted(oldJson.isCompleted());
    
        // Save updated object back to DB
        todoService.saveTodo(newObj);
        // return "redirect:/todo";
        return "redirect:/todo";
    }

}
