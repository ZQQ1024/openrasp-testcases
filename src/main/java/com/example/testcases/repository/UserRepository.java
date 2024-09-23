package com.example.testcases.repository;

import com.example.testcases.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User findById(String id) {
        /* 如下预处理，进行修复
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new UserRowMapper());
        */
        String sql = "SELECT * FROM users WHERE id = " + id; // payload: '-1' or '1' ='1' -- // 这里要看sql具体是怎么拼接的
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper());
        return users.isEmpty() ? new User() : users.get(0);
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(rs.getString("id"), rs.getString("name"), rs.getString("email"));
        }
    }
}
