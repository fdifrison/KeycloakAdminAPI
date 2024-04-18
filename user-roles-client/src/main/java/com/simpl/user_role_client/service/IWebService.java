package com.simpl.user_role_client.service;

import com.simpl.user_role_client.dto.RoleDto;
import com.simpl.user_role_client.dto.UserDto;

import java.util.List;

public interface IWebService {

    List<UserDto> getUsers();
    List<RoleDto> getRoles();
    void addUser(UserDto user);
    void addRole(RoleDto role);
}
