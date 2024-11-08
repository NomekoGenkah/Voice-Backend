package com.back.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;

public class UserTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepository;

    public void testSaveUser() {
        // Crear un usuario para probar
        String username = "testuser";
        double[][] matrix = {{1.1, 2.2}, {3.3, 4.4}};
        User user = new User(username, matrix);

        // Guardar el usuario en la base de datos
        userRepository.save(user);

        // Buscar el usuario en la base de datos
        Query query = new Query(Criteria.where("username").is(username));
        User foundUser = mongoTemplate.findOne(query, User.class);

        // Verificar si el usuario fue guardado correctamente
        if (foundUser != null) {
            System.out.println("Usuario guardado correctamente: " + foundUser.getUsername());
        } else {
            System.out.println("Error al guardar el usuario");
        }
    }
}
