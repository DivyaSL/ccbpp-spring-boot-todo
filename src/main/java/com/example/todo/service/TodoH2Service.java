package com.example.todo.service;

import com.example.todo.repository.*;
import com.example.todo.model.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.*;

@Service
public class TodoH2Service implements TodoRepository {

    @Autowired 
    private JdbcTemplate db;

    @Override 
    public ArrayList<Todo> getTodoList(){
        List<Todo> todoList = db.query("SELECT * FROM TODOLIST", new TodoRowMapper());
        ArrayList<Todo> todos = new ArrayList<>(todoList);
        return todos;
    }

    @Override 
    public Todo getTodoById(int id){
        try {
            Todo todo = db.queryForObject("SELECT * FROM TODOLIST WHERE id = ?", new TodoRowMapper(), id);
            return todo;
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        
    }

    @Override 
    public Todo addTodo(Todo todo){
        db.update("INSERT INTO TODOLIST(todo, status, priority) VALUES(?, ?, ?)", todo.getTodo(), todo.getStatus(), todo.getPriority());
        Todo addedTodo = db.queryForObject("SELECT * FROM TODOLIST WHERE todo = ?", new TodoRowMapper(), todo.getTodo());
        return addedTodo;
    }

    @Override 
    public Todo updateTodo(int id, Todo todo){
        if(todo.getTodo() != null){
            db.update("UPDATE TODOLIST SET todo = ? WHERE id = ?", todo.getTodo(), id);
        }
        if(todo.getStatus() != null){
            db.update("UPDATE TODOLIST SET status = ? WHERE id = ?", todo.getStatus(), id);
        }
        if(todo.getPriority() != null){
            db.update("UPDATE TODOLIST SET priority = ? WHERE id = ?", todo.getPriority(), id);
        }
        return getTodoById(id);
    }

    @Override 
    public void deleteTodo(int id){
        db.update("DELETE FROM TODOLIST WHERE id = ?", id); 
    }
}
