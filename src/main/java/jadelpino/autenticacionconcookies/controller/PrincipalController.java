package jadelpino.autenticacionconcookies.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
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
            // Si el usuario y la contraseña coinciden
            if (usuarios.containsKey(usuario) && password.equals(usuarios.get(usuario))) {
                // creamos un array de las cookies del Navegador
                Cookie[] cookies = request.getCookies();
                // Buscamos, si existe, la cookie por nombre del usuario
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(usuario)) {
                        try {
                            // Deserializamos en "=" para acceder al contador
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

                informacionCliente(request,model);

                model.addAttribute("usuario", "Bienvenido " + usuario + "!");
                model.addAttribute("contador", contador);
                return "areaPersonal";
            }
            model.addAttribute("error", "El la contraseña "+ usuario +" no es correcta");
            return "password";
    }


 // #################### Informacion Cliente ##########################

    //@GetMapping("informacion")
    public void informacionCliente(HttpServletRequest request, Model model) {
        // Obtiene la dirección IP del cliente
        String DireccionIp = request.getRemoteAddr();
        // Obtiene la información del navegador
        String navegador = identificarNavegador(request);
        // Obtiene el Sistema Opertaivo
        String SO = obtenerSistemaOperativo(request);
        // Obtiene el motor de renderizado del navegador
        String renderizado = obtenerMotorRenderizado(request);
        // Obtiene el encabezado "Host" de la solicitud HTTP
        String hostSolicitado = request.getHeader("Host");
        // Obtiene el idioma y locale principal del navegador
        Locale localePrincipal = request.getLocale();

        model.addAttribute("direccionIp", DireccionIp);
        model.addAttribute("navegador", navegador);
        model.addAttribute("SO", SO);
        model.addAttribute("renderizado", renderizado);
        model.addAttribute("hostSolicitado", hostSolicitado);
        model.addAttribute("localePrincipal", localePrincipal.toString());

    }

    // Método para extraer el sistema operativo del User-Agent
    private String obtenerSistemaOperativo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) return "Desconocido";
        // Identifica los sistemas operativos
        if (userAgent.contains("Windows")) {
            return "Windows";
        } else if (userAgent.contains("Macintosh") || userAgent.contains("Mac OS X")) {
            return "macOS";
        } else if (userAgent.contains("Linux")) {
            return "Linux";
        }
        return "Desconocido";
    }

    private String obtenerMotorRenderizado(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) {
            return "Desconocido";
        }
        // Detectar motores de renderizado según el User-Agent
        if (userAgent.contains("Gecko") && !userAgent.contains("WebKit")) {
            return "Gecko (Firefox)";
        } else if (userAgent.contains("AppleWebKit") && userAgent.contains("Chrome")) {
            return "Blink (Chromium-based)";
        } else if (userAgent.contains("AppleWebKit")) {
            return "WebKit (Safari)";
        }

        return "Desconocido";
    }

    public String identificarNavegador(HttpServletRequest request) {
        // Accede al User Agent
        String userAgent = request.getHeader("User-Agent");
        // Si el User Agent  null
        if (userAgent == null) {
            return "Desconocido";
        }
        // Detectar el navegador según el User-Agent
        if (userAgent.contains("Chrome") && !userAgent.contains("Chromium")) {
            return "Google Chrome";
        } else if (userAgent.contains("Firefox")) {
            return "Mozilla Firefox";
        } else if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) {
            return "Apple Safari";
        } else if (userAgent.contains("Edg")) {
            return "Microsoft Edge";
        } else if (userAgent.contains("OPR") || userAgent.contains("Opera")) {
            return "Opera";
        } else if (userAgent.contains("Trident") || userAgent.contains("MSIE")) {
            return "Internet Explorer";
        }

        return "Navegador desconocido";
    }



}








