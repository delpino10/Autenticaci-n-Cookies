package jadelpino.autenticacionconcookies.controller;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.HashMap;

@Controller
public class PrincipalController {


    private Map<String, String> usuarios = new HashMap<>();

    public PrincipalController() {
        // Agregar usuarios de prueba
        usuarios.put("admin", "1234");
        usuarios.put("user", "password");
    }



        @GetMapping("/login")
        public String userPage(Model model) {
        return "user";
    }

        @PostMapping("/check-usuario")
        public String submitUser(@RequestParam("usuario") String usuario, Model model) {
        model.addAttribute("usuario", usuario);
            if (!usuarios.containsKey(usuario)) {
                model.addAttribute("error", "El nombre del usuario " + usuario + " no es correcto");
                return "user";
            }
        return "redirect:/contrasenia?usuario=" + usuario;
    }

        @GetMapping("/contrasenia")
        public String passwordPage(@RequestParam("usuario") String usuario, Model model) {
        model.addAttribute("usuario", usuario);
        return "password";
    }

        @PostMapping("/updatePassword")
        public String updatePassword(
                @RequestParam(value = "usuario", required = false) String usuario,
                @PathVariable
            @RequestParam("contrasenia") String password,
            Model model) {

            if (usuarios.containsKey(usuario) && password.equals(usuarios.get(usuario))) {
                return "areaPersonal";
            }
            model.addAttribute("error", "El la contraseña no es correcta");
            return "password";
    }



}








