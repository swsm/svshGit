package com.swsm.system.main.action;

import com.alibaba.fastjson.JSON;
import com.core.exception.BaseException;
import com.core.tools.PageInfo;
import com.core.tools.format.MesJsonUtil;
import com.swsm.system.constant.ActionConstants;
import com.swsm.system.login.session.SessionManage;
import com.swsm.system.login.session.UserSession;
import com.swsm.system.main.service.IMainService;
import com.swsm.system.main.vo.ProductModel;
import com.swsm.system.main.vo.TreeNodeModel;
import com.swsm.system.product.ProductUtil;
import com.swsm.system.system.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <p>
 * ClassName: MainAction
 * </p>
 * <p>
 * Description: 主页控制器
 * </p>
 */
@Controller
public class MainAction {
    /**
     * 日志信息
     */
    private static Logger logger = LoggerFactory.getLogger(MainAction.class);
    /**
     * mainService
     */
    @Autowired
    @Qualifier("mainService")
    private IMainService mainService;

    /**
     * 
     * <p>
     * Description: 获取MES定义的产品信息
     * </p>
     * 
     * @param request 请求信息
     * @return 产品信息
     * @throws Exception 异常
     */
    @ResponseBody
    @RequestMapping(value = "/main/getTopInfo.mvc", produces = "application/json; charset=utf-8")
    public String getTopInfo(HttpServletRequest request) throws Exception {
        ProductModel product;
        try {
            product = ProductUtil.getProduct();
            return JSON.toJSONString(product);
        } catch (BaseException e) {
            throw e;
        }

    }

    /**
     * 
     * <p>
     * Description: 获取资源
     * </p>
     * 
     * @param request 请求数据
     * @return 资源JSON字符串
     * @throws Exception 异常
     */
    @ResponseBody
    @RequestMapping(value = "/main/getResource.mvc", produces = "application/json; charset=utf-8")
    public String getResource(HttpServletRequest request) throws Exception {
        try {
            String parentId = request.getParameter("node");
            if (parentId == null || "root".equals(parentId)) {
                parentId = ActionConstants.ROOT_NODE_ID;
            }
            UserSession userSession;
            userSession = SessionManage.getSessionManage().getUserSession(request.getSession(false));
            Resource[] resources;
            resources = this.mainService.getResourcesByParentId(parentId, userSession.getLoginName());
            List<TreeNodeModel> list;
            list = new ArrayList<TreeNodeModel>();
            for (Resource res : resources) {
                TreeNodeModel model;
                model = new TreeNodeModel();
                model.setId(res.getId());
                model.setText(res.getResName());
                model.setLeaf(false);
                if ("1".equals(res.getModualFalg())) {
                    model.setLeaf(true);
                } else {
                    if ("1".equals(res.getResType()) && "".equals(res.getParentId())) { //外层目录节点
                        if ("".equals(res.getIconCls()) || null == res.getIconCls()) {
                            model.setIconCls(" icon-lefticon_18px_xitong  ");
                        } else {
                            model.setIconCls(res.getIconCls());
                        }
                    } else {
                        model.setIconCls(" x-tree-icon-leaf ");
                    }
                }
                model.setResCode(res.getResCode());
                list.add(model);
            }
            String str;
            str = MesJsonUtil.toJsonStr(list, new PageInfo(), null);
            logger.info(str);
            return str;
        } catch (BaseException e) {
            throw e;
        }

    }
}
