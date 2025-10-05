package com.cdac.service;

import java.util.List;

import com.cdac.dto.SignupReqDTO;
import com.cdac.dto.UserRequestDTO;
import com.cdac.dto.UserRespDTO;

public interface UserService 
{

	UserRespDTO signUp(SignupReqDTO dto);
	UserRespDTO getUserById(Long id);
    UserRespDTO getUserByEmail(String email);
    List<UserRespDTO> getAllUsers();

    UserRespDTO updateUser(Long id, UserRequestDTO userDTO);
    UserRespDTO updateUserByEmail(String email, UserRequestDTO userDTO);

    void deleteUser(Long id);
    void changePassword(Long id, String newPassword);

}
