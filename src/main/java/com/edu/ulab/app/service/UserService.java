package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import org.springframework.stereotype.Repository;

@Repository
public interface UserService {
    UserDto createUser(UserDto userDto);

    Long updateUser(UserDto userDto);

    Person getUserByFullNameAndTitleFromUserDto(UserDto userDto);

    UserDto getUserById(Long id);

    void deleteUserByUserIdCheckedIsInDB(Long id);
}
