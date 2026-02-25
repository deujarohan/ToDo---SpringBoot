package com.todo_list.todo_web_app.Repository;


import com.todo_list.todo_web_app.Model.ToDo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface todoRepository extends MongoRepository<ToDo, String> {

}
