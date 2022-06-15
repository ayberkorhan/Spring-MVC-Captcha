package tr.edu.duzce.spring.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import tr.edu.duzce.spring.dao.CaptchaDao;
import tr.edu.duzce.spring.model.Captcha;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author mahmutcandurak
 */

@Service
public class CaptchaService {

    private final CaptchaDao captchaDao;
    public static final String FILE_TYPE = "jpeg";

    public CaptchaService(CaptchaDao captchaDao) {
        this.captchaDao = captchaDao;
    }

    // Rastgele bir string generate eder
    public String generateCaptcha(int captchaLength) {
        String captcha = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

        StringBuffer captchaBuffer = new StringBuffer();
        Random random = new Random();

        while(captchaBuffer.length() < captchaLength) {
            int index = (int) (random.nextFloat() * captcha.length());
            captchaBuffer.append(captcha.substring(index, index+1));
        }
        return captchaBuffer.toString();
    }



    //generateCaptcha methodunda generate edilen captcha yı alır byte arraye çevirir VE DB YE KAYDEDER
    @Transactional
    public Object generateCaptchaImage (HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Progma", "no-cache");
        response.setDateHeader("Max-Age", 0);

        String captcha = generateCaptcha(5);

        int width = 160, height = 35;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.OPAQUE);
        Graphics graphics = bufferedImage.createGraphics();
        graphics.setFont(new Font("Arial", Font.BOLD, 20));
        graphics.setColor(new Color(169, 169, 169));
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(new Color(255, 255, 255));
        graphics.drawString(captcha, 20, 25);

        HttpSession session = request.getSession(true);
        session.setAttribute("captcha", captcha);

        OutputStream outputStream = response.getOutputStream();
        ImageIO.setUseCache(false);
        boolean image = ImageIO.write(bufferedImage, FILE_TYPE, outputStream);
        outputStream.close();
        byte [] image2 = toByteArray(bufferedImage,"png");


        // SAVE CAPTCHA
        Captcha captcha1 = new Captcha();
        captcha1.setCaptcha(image2);
        captcha1.setDescription(captcha);
        saveCaptcha(captcha1);

        return image;
    }

    //BUFFERED IMAGE TO BYTE ARRAY
    public static byte[] toByteArray(BufferedImage bi, String format)
            throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;

    }

    @Transactional()
    // SAVE METHOD
    public boolean saveCaptcha(Captcha captcha) {
        return captchaDao.saveOrUpdateObject(captcha);
    }

    @Transactional()
    // BÜTÜN ID LERİ ÇEKER
    protected List<Long> getAllCaptchaId(){
        List<Captcha> captchaList = captchaDao.getAllCaptchaId();
        List<Long> idList = captchaList
                .stream()
                .map(i -> i.getId())
                .collect(Collectors.toList());

        return idList;
    }

    // ID Listesinden random bir id seçer ve ilgili captcha nesnesini getirir.
     @Transactional()
    public Captcha getRandomCaptcha () {
        List<Long> idList = getAllCaptchaId();
        Random random = new Random();
        Long x = Long.valueOf(random.nextInt(idList.size()));
        Captcha captcha = captchaDao.getCaptchaById(idList.get(x.intValue()));

        return captcha;

    }

    @Transactional()
    // value ve id ye göre doğrulama
    public boolean verifyCaptcha(String desc,Long id) {
        Captcha captcha =  captchaDao.getCaptchaById(id);
        return captcha.getDescription().equals(desc);
    }



}
