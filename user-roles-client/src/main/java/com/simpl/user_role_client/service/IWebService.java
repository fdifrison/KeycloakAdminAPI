package com.simpl.user_role_client.service;

import com.simpl.user_role_client.dto.KcRoleDto;
import com.simpl.user_role_client.dto.UserDto;

import java.util.List;

public interface IWebService {

    List<UserDto> getAllUsers();
    List<KcRoleDto> getClientRoles();
    List<KcRoleDto> getRealmRoles();
    void addRandomUser();
    void addRandomClientRole();
    void addRandomRealmRole();
}
