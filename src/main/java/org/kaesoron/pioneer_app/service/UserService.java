package org.kaesoron.pioneer_app.service;

import org.kaesoron.pioneer_app.dto.*;

import java.util.List;

public interface UserService {
    UserDto getCurrentUser();
    List<UserDto> searchUsers(SearchFilterDto filter);
    void updateCurrentUser(UserUpdateRequest request);
}
