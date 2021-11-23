package net.javaguides.todoapp.dao;

import java.util.ArrayList;
import java.util.List;

import net.javaguides.todoapp.model.Todo;

/**
 * This DAO class provides CRUD database operations for the table todos in the
 * database.
 * 
 * @author Ramesh Fadatare
 *
 */

public class TodoDaoImpl implements TodoDao {
	
	List<Todo> todos = new ArrayList<>();

	

	public TodoDaoImpl() {
	}

	@Override
	public void insertTodo(Todo todo)  {
		todos.add(todo);
	}

	@Override
	public Todo selectTodo(long todoId) {
		for(Todo todo: todos) {
			if (todo.getId() == todoId) {
				return todo;
			}
		}
		return null;
	}

	@Override
	public List<Todo> selectAllTodos() {

		return todos;
	}

	@Override
	public boolean deleteTodo(int id)  {
		
		for(Todo todo: todos) {
			if(todo.getId() == id) {
				todos.remove(todo);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean updateTodo(Todo updatedTodo)  {
		
		for(Todo todo: todos) {
			if(todo.getId() == updatedTodo.getId()) {
				todo.setDescription(updatedTodo.getDescription());
				todo.setStatus(updatedTodo.getStatus());
				todo.setTargetDate(updatedTodo.getTargetDate());
				todo.setTitle(updatedTodo.getTitle());
				todo.setUsername(updatedTodo.getUsername());
				return true;
			}
		}
		
		return false;
	}
}



















