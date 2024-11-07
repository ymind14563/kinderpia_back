package sesac_3rd.sesac_3rd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManagerViewController {
    @GetMapping({"/admin", "/admin/**"})
    public String serveReactApp(){
        return "forward:/index2.html";
    }
}
