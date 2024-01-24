package FirstTask;

public class CorrectLogIn { // класс с методом для проверки пароля

    public static boolean logInIsCorrect (String name, String password) {
        if (name.equals("client") && password.equals("0000")) {
            return true;
        } else {
            return false;
        }
    }
}
