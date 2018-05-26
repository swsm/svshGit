package com.swsm.system.login.session;

import com.core.exception.BaseException;
import com.core.tools.SpringUtil;
import com.swsm.system.login.LoginUtil;
import com.swsm.system.login.service.ILoginService;
import com.swsm.system.login.token.TokenUtil;
import com.swsm.system.main.service.IMainService;
import com.swsm.system.system.model.Organ;
import com.swsm.system.system.model.Resource;
import com.swsm.system.system.model.Role;
import com.swsm.system.system.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * 
 * <p>
 * ClassName: SessionFilter
 * </p>
 * <p>
 * Description: session过滤器
 * </p>
 */
public class SessionFilter implements Filter {
    /**
     * 排除的过滤标识，配置的参数名称
     */
    private static final String LOGINPARAM = "login";

    /**
     * inverfySession
     */
    private static final String INVERFYSESSION = "inverfySession";
    /**
     * 日志信息
     */
    private static Logger logger = LoggerFactory.getLogger(SessionFilter.class);

    /**
     * 排除的过滤标识，参数值
     */
    private String loginParamValue;
    /**
     * inverfySessionValue
     */
    private String inverfySessionValue;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.loginParamValue = filterConfig.getInitParameter(LOGINPARAM);
        this.inverfySessionValue = filterConfig.getInitParameter(INVERFYSESSION);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest req;
        req = (HttpServletRequest) request;
        HttpServletResponse res;
        res = (HttpServletResponse) response;
        boolean isPass = this.passRequest(req);
        if (!isPass) {
            boolean exsistSession = this.exsistSession(req);
            if (!exsistSession) {
                this.forwardLoginPage(req, res);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * 
     * <p>
     * Description: /dc以及login.jsp的请求不通过session验证,其他请求均通过session验证
     * </p>
     * 
     * @param request 请求对象
     * @return 是否通过
     */
    private boolean passRequest(HttpServletRequest request) {
        String uri;
        uri = request.getRequestURI();
        boolean isLoginUri = false;
        boolean isInverfySessionUri = false;
        if (this.loginParamValue != null && uri.indexOf(this.loginParamValue) > -1) {
            isLoginUri = true;
        }
        if (this.inverfySessionValue != null) {
            String[] inverfyValues = this.inverfySessionValue.split(",");
            for (String v : inverfyValues) {
                if (uri.indexOf(v) != -1) {
                    isInverfySessionUri = true;
                }
            }
        }
        if (isLoginUri || isInverfySessionUri) {
            return true;
        } else if (uri.indexOf(".jsp") > -1 || uri.indexOf(".mvc") > -1) {
            return false;
        }
        return true;
    }

    /**
     * 
     * <p>
     * Description: 判断是否存在session
     * </p>
     * 
     * @param request 请求对象
     * @return 是否存在session
     */
    @SuppressWarnings("unchecked")
    private boolean exsistSession(HttpServletRequest request) {
        HttpSession session;
        session = request.getSession(false);
        if (session != null) {
            UserSession userSession;
            userSession = SessionManage.getSessionManage().getUserSession(session);
            if (userSession != null && userSession.getUserId() != null) {
                return true;
            }
        }
        String token = LoginUtil.getToken(request);
        if (token != null) {
            String[] arr = TokenUtil.getUserInfo(token);
            try {
                ILoginService loginService = (ILoginService) SpringUtil.getBean(request, "loginService");
                Map<String, Object> fullUser = loginService.findUserFullInfo(arr[0]);
                User user = (User) fullUser.get("user");
                Set<Organ> organSet = fullUser.get("organs") != null ? (Set<Organ>) fullUser.get("organs") : null;
                Set<Role> roleSet = fullUser.get("roles") != null ? (Set<Role>) fullUser.get("roles") : null;
                String[] resCodes = new String[0];
                IMainService mainService = (IMainService) SpringUtil.getBean(request, "mainService");
                Resource[] res = mainService.getHavResByLoginName(user.getUsername());
                resCodes = new String[res.length];
                for (int i = 0; i < res.length; i++) {
                    resCodes[i] = res[i].getResCode();
                }
                LoginUtil.loadSession(resCodes, user, organSet, roleSet, request, null);
                return true;
            } catch (BaseException e) {
                logger.error(e.getMessage(), e);
            }

        }
        return false;
    }

    /**
     * 
     * <p>
     * Description: 跳转到登录页面
     * </p>
     * 
     * @param request 请求
     * @param response 响应
     * @throws IOException IO异常
     * @throws ServletException ServletException
     */
    private void forwardLoginPage(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        String xRequestedWith;
        xRequestedWith = request.getHeader("x-requested-with");
        if (xRequestedWith != null) {
            response.getWriter().write("eval(\"window.parent.location.href='login.jsp'\")");
        } else {
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
    }

}
