package com.yyan.controller;

import com.yyan.serviceImpl.BpmServiceImpl;
import com.yyan.utils.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/api/bpm/")
public class BpmController extends BaseController {


    @Autowired
    private BpmServiceImpl bpmService;


    /**
     * 流程部署  act_re_deployment   ACT_RE_PROCDEF
     * todo 通过前端传递 bpmn 文件流
     *
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
     *
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
     * 查看流程图
     *
     * @return
     */
    @RequestMapping("/select/processImg")
    @ResponseBody
    public Map<String, Object> selectProcessImg(@RequestParam String deploymentId) {
        try {
            String data = bpmService.selectProcessImg(deploymentId);
            return this.buildSuccess(data);
        } catch (Exception exp) {
            System.out.println(exp);
            return this.buildError(exp.getMessage());
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
