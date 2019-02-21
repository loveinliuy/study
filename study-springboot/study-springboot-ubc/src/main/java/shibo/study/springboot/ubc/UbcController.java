package shibo.study.springboot.ubc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * collect info and return a mock pic
 *
 * @author zhangshibo
 */
@Slf4j
@Controller
public class UbcController {

    private static final BufferedImage IMG = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

    @RequestMapping("/1.gif")
    public void ubc(HttpServletRequest request, HttpServletResponse response, UserBehaviourInfo info) {
        info.setRemoteHost(request.getRemoteHost());
        info.setRemotePort(request.getRemotePort());
        log.debug("get ubc: {}", info);
        response.setContentType("image/gif");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(IMG, "GIF", os);
        } catch (IOException e) {
            log.error("can not get output stream from response.", e);
        }
    }

}
