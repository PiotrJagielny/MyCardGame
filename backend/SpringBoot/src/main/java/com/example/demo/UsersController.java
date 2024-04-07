package com.example.demo;

import com.example.demo.cardsPersistence.DecksDatabase;
import com.example.demo.cardsPersistence.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/Users")
public class UsersController {

    @GetMapping(path = "registerUser/{username}/{password}")
    @CrossOrigin
    public boolean registerUser(@PathVariable String username, @PathVariable String password){
        return DecksDatabase.registerUser(username, password);
    }

    @GetMapping(path = "registerAdmin/{username}/{password}")
    @CrossOrigin
    public boolean registerAdmin(@PathVariable String username, @PathVariable String password){
        return DecksDatabase.registerAdmin(username, password);
    }

    @GetMapping(path = "loginUser/{username}/{password}")
    @CrossOrigin
    public boolean checkUserData(@PathVariable String username, @PathVariable String password){
        return DecksDatabase.checkUserData(username, password);
    }

    @GetMapping(path = "loginAdmin/{username}/{password}")
    @CrossOrigin
    public boolean checkAdminData(@PathVariable String username, @PathVariable String password){
        return DecksDatabase.checkAdminData(username, password);
    }

    @GetMapping(path = "getUsers")
    @CrossOrigin
    public List<User> getUsers(){
        return DecksDatabase.getUsers();
    }

    @GetMapping(path = "deleteUser/{username}")
    @CrossOrigin
    public void deleteUser(@PathVariable String username){
        DecksDatabase.deleteUser(username);
    }
}
