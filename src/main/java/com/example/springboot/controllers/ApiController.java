package com.example.springboot.controllers;

import com.example.springboot.models.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Base64;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class ApiController {
    private static final String URL = "jdbc:postgresql://ep-jolly-brook-a297kivr.eu-central-1.aws.neon.tech/MyDataBase?user=andrey678a&password=6xNohVCgZry7&sslmode=require";


    @PostMapping("/saveUserDataBase")
    public String saveUserData(HttpServletResponse response, @RequestBody User user) {
        try (Connection connection = getConnection()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, name TEXT NOT NULL UNIQUE, email TEXT NOT NULL, password TEXT NOT NULL)";
            connection.createStatement().executeUpdate(createTableSQL);
            String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, user.getName());
                statement.setString(2, user.getEmail());
                statement.setString(3, user.getPassword());
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    String selectQuery = "SELECT * FROM users WHERE name=?;";
                    try (PreparedStatement statement2 = connection.prepareStatement(selectQuery)) {
                        statement2.setString(1, user.getName());
                        ResultSet rs = statement2.executeQuery();
                        rs.next();
                        if (rs.next()) {
                            if (Objects.equals(rs.getString("password"), user.getPassword())) {
                                Cookie cookie = new Cookie("uid", encrypt(Integer.toString(rs.getInt("id")), "8jmT3zAqK9JMN8bjcNrs4ZO2WB3MYALY"));
                                cookie.setPath("/");
                                cookie.setMaxAge(86400);
                                response.addCookie(cookie);
                                response.setContentType("text/plain");
                            }
                        }
                    }
                    catch (NoSuchPaddingException | BadPaddingException | NoSuchAlgorithmException |
                           IllegalBlockSizeException | InvalidKeyException _) {
                        return null;
                    }
                    return "ok";
                }
            }
        } catch (SQLException _) {
        }
        return null;
    }
    @PostMapping("/login")
    public String login(HttpServletResponse response, @RequestBody User user) {
        try (Connection connection = getConnection()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, name TEXT NOT NULL, email TEXT NOT NULL, password TEXT NOT NULL)";
            connection.createStatement().executeUpdate(createTableSQL);
            String selectQuery = "SELECT * FROM users WHERE name=?;";
            try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
                statement.setString(1, user.getName());
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    if (Objects.equals(rs.getString("password"), user.getPassword())) {
                        Cookie cookie = new Cookie("uid", encrypt(Integer.toString(rs.getInt("id")), "8jmT3zAqK9JMN8bjcNrs4ZO2WB3MYALY"));
                        cookie.setPath("/");
                        cookie.setMaxAge(86400);
                        response.addCookie(cookie);
                        response.setContentType("text/plain");
                        System.out.println(3333);
                        return "ok";
                    }
                }
            }
            catch (NoSuchPaddingException | InvalidKeyException | BadPaddingException | NoSuchAlgorithmException |
                   IllegalBlockSizeException _) {
            }
        } catch (SQLException _) {
        }
        return null;
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static String encrypt(String originalText, String secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secretKey.getBytes(), "AES"));
        byte[] encryptedBytes = cipher.doFinal(originalText.getBytes());

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    @GetMapping("/user")
    public User getUser(@CookieValue(value = "uid", required = false) String uid) {
        if (uid == null)
            return null;
        try (Connection connection = getConnection()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, name TEXT NOT NULL, email TEXT NOT NULL, password TEXT NOT NULL)";
            connection.createStatement().executeUpdate(createTableSQL);
            String selectQuery = "SELECT * FROM users;";
            ResultSet rs = connection.createStatement().executeQuery(selectQuery);
            System.out.println(uid);
            while (rs.next()) {
                System.out.println(rs.getString("email"));
                if (Objects.equals(encrypt(rs.getString("id"), "8jmT3zAqK9JMN8bjcNrs4ZO2WB3MYALY"), uid)) {
                    return new User(rs.getString("name"), rs.getString("password"), rs.getString("email"));
                }
            }
        } catch (SQLException | NoSuchPaddingException | InvalidKeyException | BadPaddingException |
                 NoSuchAlgorithmException | IllegalBlockSizeException _) {
        }
        return null;
    }
}



