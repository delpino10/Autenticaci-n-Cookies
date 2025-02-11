package jadelpino.autenticacionconcookies.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
//@Getter
public class Colecciones {

    public static Map<String, String> usuarioContrasenia = new HashMap<>();

    static {
        usuarioContrasenia.put("alfa", "admin123");
        usuarioContrasenia.put("beta", "password1");
        usuarioContrasenia.put("gamma", "password2");
    }


    public static Map<String, String> getusuarioContrasenia() {
        return usuarioContrasenia;
    }
}
