package com.swsm.system.action;

import com.swsm.constant.ActionConstants;
import com.swsm.login.session.SessionManage;
import com.swsm.login.session.UserSession;
import com.swsm.system.service.IAuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


/**
 * 
 * <p>
 * ClassName: AuthorAction
 * </p>
 * <p>
 * Description:权限控制器
 * </p>
 */
@Controller
public class AuthorAction {
    /**
     * 日志信息
     */
    private static Logger logger = LoggerFactory.getLogger(AuthorAction.class);
    /**
     * 权限服务
     */
    @Autowired
    @Qualifier("authorService")
    private IAuthorService authorService;

    /**
     * 
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param request 请求
     * @return boolean
     * @throws Exception 异常
     */
    @RequestMapping(value = "/main/system/hasAuthor.mvc")
    @ResponseBody
    public boolean hasAuthor(HttpServletRequest request) throws Exception {
        UserSession userSession;
        userSession = SessionManage.getSessionManage().getUserSession(request.getSession());
        String loginName = null;
        if (userSession != null) {
            loginName = userSession.getLoginName();
        }
        if (ActionConstants.ADMIN_USER.equals(loginName)) {
            return true;
        }
        String resCode;
        resCode = request.getParameter("resCode");
        return this.authorService.hasAuthor(loginName, resCode);
    }

}
