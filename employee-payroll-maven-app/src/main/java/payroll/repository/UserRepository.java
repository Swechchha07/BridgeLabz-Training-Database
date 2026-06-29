package payroll.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import payroll.model.User;

import java.util.List;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Maps a single row of the users table into a User object
    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {

        User user = new User();

        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));

        return user;

    };

    //====================================================
    // FIND BY USERNAME
    //====================================================

    public User findByUsername(String username) {

        String sql =
                "SELECT * FROM users WHERE username=?";

        List<User> list =
                jdbcTemplate.query(sql, userRowMapper, username);

        if (list.isEmpty()) {

            return null;

        }

        return list.get(0);

    }

    //====================================================
    // REGISTER USER
    //====================================================

    public void registerUser(String username,
                             String hashedPassword,
                             String email,
                             String role) {

        String sql =
                "INSERT INTO users(username,password,email,role) VALUES(?,?,?,?)";

        jdbcTemplate.update(sql,
                username,
                hashedPassword,
                email,
                role);

    }

}
