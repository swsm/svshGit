package com.swsm.system.login.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
/**
 * 
 * <p>ClassName: GzipFilter</p>
 * <p>Description: GzipFilter</p>
 */
public class GzipFilter implements Filter {

    /**
     * ctx
     */
    private ServletContext ctx;
    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(GzipFilter.class);
    /**
     * contextPath
     */
    private String contextPath;

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest req;
        req = (HttpServletRequest) request;
        HttpServletResponse res;
        res = (HttpServletResponse) response;
        String uri;
        uri = req.getRequestURI();
        String accept;
        accept = req.getHeader("Accept-Encoding");
        InputStream in = null;
        contextPath = ctx.getContextPath();
        uri = uri.substring(contextPath.length());
        if (accept != null && accept.contains("gzip")) {
            int idx;
            idx = uri.indexOf("?");
            boolean isGz = false;
            if (idx == -1) {
                isGz = (in = ctx.getResourceAsStream(uri + ".gz")) != null;
            }
            if (idx > -1) {
                String temp = uri.substring(0, idx - 1) + ".gz" + uri.substring(idx);
                uri = temp;
                isGz = (in = ctx.getResourceAsStream(uri)) != null;
            }
            if (isGz) {
                logger.info("start getting gzip file " + uri);
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                byte[] b = new byte[1024 * 8];
                int read = 0;
                while ((read = in.read(b)) >= 0) {
                    bout.write(b, 0, read);
                }
                in.close();

                res.setHeader("Content-Encoding", "gzip");
                res.setContentType("application/javascript;charset=UTF-8");
                res.setContentLength(bout.size());

                ServletOutputStream out = res.getOutputStream();
                out.write(bout.toByteArray());
                out.flush();
                logger.info("finish getting gzip file " + uri);
                return;
            } else {
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        ctx = config.getServletContext();
    }

}
