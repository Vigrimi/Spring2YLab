package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.enums.IntConstants;
import com.edu.ulab.app.enums.LongConstants;
import com.edu.ulab.app.enums.StringConstatnts;
import com.edu.ulab.app.exception.MyException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImplTemplate implements UserService {
    private final UserMapper userMapper;
    private final JdbcTemplate jdbcTemplate;

    public UserServiceImplTemplate(UserMapper userMapper, JdbcTemplate jdbcTemplate) {
        this.userMapper = userMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    private PreparedStatement getPreparedStatementForPerson(Connection connection,
                                                            String SQLExpression, UserDto userDto) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SQLExpression, new String[]{"id"});
        ps.setString(1, userDto.getFullName());
        ps.setString(2, userDto.getTitle());
        ps.setLong(3, userDto.getAge());
        return ps;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("getUserById begins - userDto: {}", userDto);
        final String INSERT_SQL = "INSERT INTO PERSON(FULL_NAME, TITLE, AGE) VALUES (?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        log.info("getUserById begins - keyHolder: {}", keyHolder.getKeyList());
        jdbcTemplate.update(
                connection -> {
                    return getPreparedStatementForPerson(connection, INSERT_SQL, userDto);
                }, keyHolder);
        log.info("getUserById finished - keyHolder: {}", keyHolder.getKeyList());
        userDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        log.info("getUserById finished - userDto: {}", userDto);
        return userDto;
    }

    @Override
    public Long updateUser(UserDto userDto) {
        // реализовать недстающие методы
        return null;
    }

    @Override
    public Person getUserByFullNameAndTitleFromUserDto(UserDto userDto) {
        return null;
    }

    @Override
    public UserDto getUserById(Long id) {
        log.info("-- getUserById id: {}", id);
        if (id <= LongConstants.USER_ID_NOT_FOUND_LONG.getDigits()){
            throw new MyException(StringConstatnts.INPUTED_USER_ID_IS_UNREAL.getText());
        }
        Person person = getEmptyPerson();
        final String FIND_BY_ID_SQL = "SELECT * FROM PERSON WHERE id=?";
        try {
            person = jdbcTemplate.queryForObject(FIND_BY_ID_SQL,
                    BeanPropertyRowMapper.newInstance(Person.class), id);
        } catch (Exception e){
            log.error(e.toString());
            throw new MyException(e.toString());
        }
        log.info("getUserById done from DB - person: {}", person);
        if (person == null || person.getId() == LongConstants.USER_ID_NOT_FOUND_LONG.getDigits()) {
            throw new MyException(StringConstatnts.INPUTED_USER_ID_WAS_NOT_FOUND_IN_DB.getText());
        }
        UserDto userDto = userMapper.personToUserDto(person);
        log.info("getUserById finished - userDto: {}", userDto);
        return userDto;
    }

    @Override
    public void deleteUserByUserIdCheckedIsInDB(Long id) {
        // реализовать недстающие методы
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
