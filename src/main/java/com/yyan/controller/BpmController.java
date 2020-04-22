package com.yyan.controller;

import com.yyan.serviceImpl.BpmServiceImpl;
import com.yyan.utils.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Controller
@RequestMapping("/api/bpm/")
public class BpmController extends BaseController {


    @Autowired
    private BpmServiceImpl bpmService;


    /**
     * 流程部署  act_re_deployment   ACT_RE_PROCDEF
     * todo 通过前端传递 bpmn 文件流
     * @return
     */
    @RequestMapping("/insert/deployment")
    @ResponseBody
    public Map<String, Object> insertDeployment() {
        try {
            this.bpmService.deployment();
            return this.buildSuccess();
        } catch (Exception exp) {
            System.out.println(exp);
            return this.buildError(exp.getMessage());
        }
    }



    /**
     * 流程删除  act_re_deployment   ACT_RE_PROCDEF
     * @return
     */
    @RequestMapping("/delete/deployment")
    @ResponseBody
    public Map<String, Object> deleteDeployment(@RequestParam String deploymentId) {
        try {
            this.bpmService.deleteProcessDeployment(deploymentId);
            return this.buildSuccess();
        } catch (Exception exp) {
            System.out.println(exp);
            return this.buildError(exp.getMessage());
        }
    }



    /**
     * 流程删除  act_re_deployment   ACT_RE_PROCDEF
     * @return
     */
    @RequestMapping("/select/processImg")
    @ResponseBody
    public void selectProcessImg(@RequestParam String deploymentId, HttpServletResponse response) {
      InputStream stream= bpmService.selectProcessImg(deploymentId);

        System.out.println("stream");
        System.out.println(stream);

        byte[] bytes = new byte[0];
        try {
            bytes = new byte[stream.available()];
            stream.read(bytes);
            String str = new String(bytes);
            System.out.println("str");
            System.out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }




        try {

            BufferedImage image=ImageIO.read(stream);

            ServletOutputStream outputStream=response.getOutputStream();
            ImageIO.write(image,"JPEG",outputStream);
            stream.close();
            outputStream.close();

        } catch (Exception exp) {
            System.out.println(exp);
        }
    }




    /**
     * 查看流程部署 act_re_deployment
     *
     * @return
     */
    @RequestMapping("/select/deployment")
    @ResponseBody
    public Map<String, Object> selectProcessDeployment(@RequestBody Map map) {
        try {
            Map newMap = this.bpmService.selectListProcessDeployment(map);
            return this.buildSuccess(newMap);
        } catch (Exception exp) {
            System.out.println(exp);
            return this.buildError(exp.getMessage());
        }
    }


    /**
     * 查看流程定义 ACT_RE_PROCDEF
     *
     * @return
     */
    @RequestMapping("/select/definition")
    @ResponseBody
    public Map<String, Object> selectProcessDefinition(@RequestBody Map map) {
        try {
            Map newMap = this.bpmService.selectListProcessDefinition(map);
            return this.buildSuccess(newMap);
        } catch (Exception exp) {
            System.out.println(exp);
            return this.buildError(exp.getMessage());
        }
    }

}
