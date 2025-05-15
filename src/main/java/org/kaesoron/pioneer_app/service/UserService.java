package org.kaesoron.pioneer_app.service;

import org.kaesoron.pioneer_app.dto.*;

import java.util.List;

public interface UserService {
    UserDto getUserById(Long userId);
    List<UserDto> searchUsers(SearchFilterDto filter);
    void updateUser(Long userId, UserUpdateRequest request);
}
