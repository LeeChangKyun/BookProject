package com.book.BookProject.user;


public interface SignupService {
    void registerUser(UserDTO userDTO);
    boolean isIdUnique(String id);
    boolean isNickUnique(String nick);
}