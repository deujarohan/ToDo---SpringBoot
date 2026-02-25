package com.todo_list.todo_web_app.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import com.todo_list.todo_web_app.Model.ToDo;
import com.todo_list.todo_web_app.Repository.todoRepository;

@Component
public class todoService {
    @Autowired
    private todoRepository todoRepo;

    public void saveTodo(ToDo todo) {
        todoRepo.save(todo);
    }

    public List<ToDo> getAllTodos() {
        return todoRepo.findAll();
    }
}


// controller --> service --> repository