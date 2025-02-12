package jadelpino.autenticacionconcookies.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        public String paginaInicio(Model model) {
        return "user";
    }

        @PostMapping("/check-usuario")
        public String checkUsuario(@RequestParam("usuario") String usuario, Model model, HttpSession session) {
        model.addAttribute("usuario", usuario);
            if (!usuarios.containsKey(usuario)) {
                model.addAttribute("error", "El nombre del usuario " + usuario + " no es correcto");
                return "user";
            }
        session.setAttribute("usuario", usuario);
        return "redirect:/contrasenia?usuario=" + usuario;

    }

        @GetMapping("/contrasenia")
        public String passwordPage(@RequestParam("usuario") String usuario, Model model) {
        model.addAttribute("usuario", usuario);
        return "password";
    }

        @PostMapping("/check-password")
        public String updatePassword(
            @RequestParam("contrasenia") String password,
            HttpSession session,
            HttpServletResponse response,
            HttpServletRequest request,
            Model model) {

            // Obtener el usuario de la sesión
            String usuario = (String) session.getAttribute("usuario");
            int contador = 0;
            if (usuarios.containsKey(usuario) && password.equals(usuarios.get(usuario))) {
                Cookie[] cookies = request.getCookies();
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(usuario)) {
                        try {
                            contador = Integer.parseInt(cookie.getValue().split("=")[1]);
                        }catch (NumberFormatException  | ArrayIndexOutOfBoundsException e) {
                            contador = 0;
                        }
                    }
                }


                // Incrementar contador de visitas
                contador++;

                //Serializar de Nuevo
                String nuevoValor = "contador=" + contador ;
                // Crear una nueva cookie con el contador actualizado
                Cookie nuevaCookie = new Cookie(usuario, nuevoValor);
                nuevaCookie.setPath("/"); // Disponible en toda la aplicación
                response.addCookie(nuevaCookie);


                model.addAttribute("usuario", "Bienvenido " + usuario + "!");
                model.addAttribute("contador", contador);
                return "areaPersonal";
            }
            model.addAttribute("error", "El la contraseña "+ usuario +" no es correcta");
            return "password";
    }



}








