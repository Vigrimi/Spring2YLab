package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.enums.IntConstants;
import com.edu.ulab.app.enums.LongConstants;
import com.edu.ulab.app.enums.StringConstatnts;
import com.edu.ulab.app.exception.MyException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.CheckDifferentData;
import com.edu.ulab.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService, CheckDifferentData {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setAge(checkValidHumanAge(userDto.getAge()));
        Person user = userMapper.userDtoToPerson(userDto);
        log.info("Mapped user: {}", user);
        Person savedUser = getEmptyPerson();
        try {
            savedUser = userRepository.save(user);
        } catch (Exception e){
            log.error(e.toString());
            throw new MyException(e.toString());
        }
        log.info("Saved user: {}", savedUser);
        return (savedUser == null) ? userMapper.personToUserDto(getEmptyPerson()) : userMapper.personToUserDto(savedUser);
    }

    @Override
    public Long updateUser(UserDto userDto) {
        Person userFoundInDB = getUserByFullNameAndTitleFromUserDto(userDto);
        log.info("updateUser - userFoundInDB: {}", userFoundInDB);
        int validAge = checkValidHumanAge(userDto.getAge());

        if (validAge == IntConstants.PERSON_UNREAL_AGE_INT.getDigits())
            throw new MyException(StringConstatnts.INPUTED_USER_UPDATED_AGE_IS_UNREAL.getText());

        userFoundInDB.setAge(validAge);
        log.info("-- updateUser corrected begins person: {}", userFoundInDB);
        try {
            userRepository.save(userFoundInDB);
        } catch (Exception e){
            log.error(e.toString());
            throw new MyException(e.toString());
        }
        log.info("-- updateUser corrected finished person: {}", userFoundInDB);
        return userFoundInDB.getId();
    }

    @Override
    public Person getUserByFullNameAndTitleFromUserDto(UserDto userDto) {
        log.info("-- getUserByFullNameAndTitleFromUserDto userDto: {}", userDto);
        if (userDto.getFullName() == null) {
            throw new MyException(StringConstatnts.INPUTED_USER_FULL_NAME_IS_EMPTY.getText());
        }
        if (userDto.getTitle() == null) {
            throw new MyException(StringConstatnts.INPUTED_USER_TITLE_IS_EMPTY.getText());
        }
        Person userFmDB = getEmptyPerson();
        try {
            userFmDB = userRepository.findByFullNameAndTitle(userDto.getFullName(), userDto.getTitle())
                    .orElse(getEmptyPerson());
        } catch (Exception e) {
            log.error(e.toString());
            throw new MyException(e.toString());
        }
        log.info("-- getUserById userFmDB: {}", userFmDB);
        if (userFmDB == null || userFmDB.getId() == LongConstants.USER_ID_NOT_FOUND_LONG.getDigits()) {
            throw new MyException(StringConstatnts.USER_WAS_NOT_FOUND_IN_DB_BY_INPUTED_DATA.getText());
        }
        return userFmDB;
    }

    @Override
    public UserDto getUserById(Long id) {
        log.info("-- getUserById id: {}", id);
        if (id <= LongConstants.USER_ID_NOT_FOUND_LONG.getDigits()){
            throw new MyException(StringConstatnts.INPUTED_USER_ID_IS_UNREAL.getText());
        }
        Person userFmDB = getEmptyPerson();
        try {
            userFmDB = userRepository.findById(id).orElse(getEmptyPerson());
        } catch (Exception e){
            log.error(e.toString());
            throw new MyException(e.toString());
        }
        log.info("-- getUserById userFmDB: {}", userFmDB);
        if (userFmDB == null || userFmDB.getId() == LongConstants.USER_ID_NOT_FOUND_LONG.getDigits()) {
            throw new MyException(StringConstatnts.INPUTED_USER_ID_WAS_NOT_FOUND_IN_DB.getText());
        }
        return userMapper.personToUserDto(userFmDB);
    }

    @Override
    public void deleteUserByUserIdCheckedIsInDB(Long id) {
        log.info("\n---- deleteUserByUserIdCheckedIsInDB begins");
        try {
            userRepository.deleteById(id);
        } catch (Exception e){
            log.error(e.toString());
            throw new MyException(e.toString());
        }
        log.info("\n---- deleteUserByUserIdCheckedIsInDB finished");
    }

    public Person getEmptyPerson() {
        log.info("\n---- getEmptyPerson");
        return Person.builder()
                .id(LongConstants.USER_ID_NOT_FOUND_LONG.getDigits())
                .fullName("Это пустышка Person")
                .title("Нет данных title Person")
                .age(IntConstants.PERSON_UNREAL_AGE_INT.getDigits())
                .build();
    }
}
