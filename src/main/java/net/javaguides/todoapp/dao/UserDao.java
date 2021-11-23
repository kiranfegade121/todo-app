package net.javaguides.todoapp.dao;

import java.util.ArrayList;
import java.util.List;

import net.javaguides.todoapp.model.LoginBean;
import net.javaguides.todoapp.model.User;

public class UserDao {
	
	List<User> users = new ArrayList<>();

	public int registerEmployee(User user) throws ClassNotFoundException {
		
		users.add(user);	
		System.out.println(user);
		System.out.println("user added");
		return 1;
	}
	
	public boolean validate(LoginBean loginBean) throws ClassNotFoundException {
		boolean status = false;
		System.out.println(users);
		for(User user: users) {			
			if (user.getUsername().equals(loginBean.getUsername()) && user.getPassword().equals(loginBean.getPassword())) {
				status = true;
				System.out.println("Authenticate success");
				break;
			}
		}
		
		return status;
		
	}

}
