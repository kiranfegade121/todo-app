package net.javaguides.todoapp.dao;

import java.util.List;

import net.javaguides.todoapp.model.Todo;

public interface TodoDao {

	void insertTodo(Todo todo);

	Todo selectTodo(long todoId);

	List<Todo> selectAllTodos();

	boolean deleteTodo(int id);

	boolean updateTodo(Todo todo);

}