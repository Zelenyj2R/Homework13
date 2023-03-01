package Webprotocol.user;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class WebProtocolMethodTest {
    public static final String DEFAULT_URI_USER = "https://jsonplaceholder.typicode.com";

    public static void main(String[] args) throws Exception {
        /*создание нового объекта */
       User user = new User();
       user.setId(1);
       user.setName("Ricardo");
       user.setUsername("Hummel");
       user.setEmail("Ricardo_Hummel12@email.com");
       User  createUser = WebProtocolMethod.userCreationMethod(URI.create(DEFAULT_URI_USER), user);
        System.out.println(createUser);

        /*обновление объекта */
        User user22 = new User();
        user22.setId(1);
        user22.setName("Ricardo");
        user22.setUsername("Hummel");
        user22.setEmail("Ricardo_Hummel12@email.com");
        User  update = WebProtocolMethod.userUpdateMethod(URI.create(DEFAULT_URI_USER),2, user22);
        System.out.println(update);

        /*удаление объекта */
        WebProtocolMethod.userDeleteMethod(URI.create(DEFAULT_URI_USER),3);

        /* информация о всех пользователях */
        List<User> informationAllUsers  = WebProtocolMethod.informationAboutAllUsers(URI.create(DEFAULT_URI_USER));
        System.out.println("All users: " + informationAllUsers.toString());

        /* получение информации о пользователе с определенным id */
        User userId = WebProtocolMethod.informationUserSpecificId(URI.create(DEFAULT_URI_USER), 4);
        System.out.println("User Id" + userId);

        /* получение информации о пользователе с определенным username */
        String username =  "Kamren";
        User name = WebProtocolMethod.userSpecificName(URI.create(DEFAULT_URI_USER), username);      // Get by username
        System.out.println("User username: " + name);

        /* Задние №2 */
        int Id = 1;
        int psId = WebProtocolMethod.lastPost(URI.create(DEFAULT_URI_USER), Id);
        String commentsJson = WebProtocolMethod.lastComments(URI.create(DEFAULT_URI_USER), Id, psId);
        WebProtocolMethod.commentsJson(commentsJson, Id, psId);
        System.out.println("*****************************************");

        /* Задние №3 */
        WebProtocolMethod.openTasksUser(URI.create(DEFAULT_URI_USER), 1);
    }
}
