package tr.edu.duzce.spring.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tr.edu.duzce.spring.model.Captcha;
import tr.edu.duzce.spring.service.CaptchaService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author mahmutcandurak
 */


@Controller
public class MainController {

    private final CaptchaService captchaService;

    public MainController(CaptchaService captchaService) {
        this.captchaService = captchaService;

    }


    @GetMapping("/")
    public ModelAndView getRandomCaptcha(){

        Captcha captcha = captchaService.getRandomCaptcha();
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("captcha",captcha);

        return mv;

    }


    @GetMapping("/generatecaptcha")
    public void generateCaptcha (HttpServletRequest request, HttpServletResponse response) throws Exception {
        captchaService.generateCaptchaImage(request,response);
    }


    // value ve id ye göre doğrualama
    @PostMapping("/verify")
    public @ResponseBody String verify (@RequestParam("value") String value,
                                        @RequestParam("id") Long id){

        JSONObject object = new JSONObject();
        object.put("verify",captchaService.verifyCaptcha(value,id));
        return object.toString();
    }


}
