package com.core.tools;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

public class WebUtil {

    private static Logger logger = LoggerFactory.getLogger(WebUtil.class);

    public static PageInfo getPage(HttpServletRequest request) throws Exception {
        String sv = WebUtil.fetch(request, "start");
        String lv = WebUtil.fetch(request, "limit");
        int start, limit;
        PageInfo pageInfo = new PageInfo();

        if (StringUtils.isEmpty(sv))
            start = 0;
        else
            start = Integer.parseInt(sv);
        if (StringUtils.isEmpty(lv))
            limit = Integer.MAX_VALUE - start;
        else
            limit = Integer.parseInt(lv);
        pageInfo.start = start;
        pageInfo.end = start + limit;
        request.setAttribute("start", start);
        request.setAttribute("end", pageInfo.end);
        pageInfo.limit = limit;
        pageInfo.count = 0;
        return pageInfo;
    }

    public static int checkPage(PageInfo pageInfo) {
        return checkPage(pageInfo, true, true);
    }

    public static int checkPage(PageInfo pageInfo, boolean paged, boolean totalCount) {
        long index = pageInfo.count;
        int result;

        if (index >= pageInfo.limit)
            result = 1;
        else if (paged && index < pageInfo.start)
            result = 2;
        else if (paged && index > pageInfo.end) {
            if (totalCount)
                result = 2;
            else
                result = 1;
        } else
            result = 0;
        if (result != 1)
            pageInfo.count++;
        return result;
    }

    public static void setTotal(StringBuilder buf, PageInfo pageInfo) {
        buf.insert(0, "{\"total\":" + Long.toString(pageInfo.count));
    }

    public static void setCb(StringBuilder buf, String cb) {
        if (cb != null) {
            buf.insert(0, cb + "(");
            buf.append(");");
        }
    }

    public static String[] getSortInfo(HttpServletRequest request) throws Exception {
        String sort = request.getParameter("sort");

        if (StringUtils.isEmpty(sort))
            return null;
        JSONObject jo = new JSONArray(sort).getJSONObject(0);
        String[] result = new String[2];
        result[0] = jo.getString("property");
        result[1] = jo.optString("direction");
        return result;
    }

    public static String encodeFilename(HttpServletRequest request, String name) throws Exception {
        String agent = StringUtil.optString(request.getHeader("user-agent")).toLowerCase();
        if (name == null)
            name = "";
        if (agent.indexOf("trident") != -1 || agent.indexOf("msie") != -1)
            return StringUtil.concat("filename=\"", encodeString(name), "\"");
        else if (agent.indexOf("opera") != -1)
            return StringUtil.concat("filename*=\"utf-8''", encodeString(name), "\"");
        else
            return StringUtil.concat("filename=\"", new String(name.getBytes("utf-8"), "ISO-8859-1"), "\"");
    }

    public static String encodeString(String str) throws Exception {
        return StringUtil.replace(URLEncoder.encode(str, "utf-8"), "+", "%20");
    }

    public static String decode(String str) throws Exception {
        if (StringUtil.isEmpty(str))
            return str;
        return new String(str.getBytes("ISO-8859-1"), "utf-8");
    }

    public static void clearUploadFile(HttpServletRequest request, List<?> list) {
        FileItem item;
        for (Object t : list) {
            item = (FileItem) t;
            if (!item.isFormField())
                SysUtil.closeInputStream((InputStream) request.getAttribute(item.getFieldName()));
            item.delete();
        }
        String uploadId = (String) request.getAttribute("sys.uploadId");
        if (uploadId != null) {
            HttpSession session = request.getSession(true);
            session.removeAttribute("sys.upread." + uploadId);
            session.removeAttribute("sys.uplen." + uploadId);
        }
    }

    public static String getUrl(String url, boolean onlyId) {
        int idx;

        if (url.startsWith("#")) {
            idx = url.indexOf('(');
            if (idx == -1)
                url = url.substring(1);
            else
                url = url.substring(1, idx);
            if (onlyId)
                return url.trim();
            else
                return "main?xwl=" + url.trim();
        } else
            return url;
    }

    public static String getIdWithUser(HttpServletRequest request, String id) throws Exception {
        String user = (String) request.getAttribute("sys.user");
        return StringUtil.concat(StringUtil.optString(user), "@", id);
    }

    public static String fetch(HttpServletRequest request, String name) {
        Object obj = request.getAttribute(name);
        String val;

        if (obj == null) {
            val = request.getParameter(name);
            if (val == null)
                return "";
            else
                return val;
        } else
            return obj.toString();
    }

    public static void response(HttpServletResponse response, Object obj) throws Exception {
        if (obj instanceof InputStream) {
            InputStream is = (InputStream) obj;
            try {
                if (response.isCommitted())
                    return;
                SysUtil.isToOs(is, response.getOutputStream());
            } finally {
                is.close();
            }
        } else {
            if (response.isCommitted())
                return;
            byte[] bytes;
            if (obj instanceof byte[])
                bytes = (byte[]) obj;
            else
                bytes = obj.toString().getBytes("utf-8");
            int len = bytes.length;
            response.setContentLength(len);
            response.getOutputStream().write(bytes);
        }
        response.flushBuffer();
    }

    public static void response(HttpServletResponse response, String obj, boolean successful) throws Exception {
        WebUtil.response(
                response,
                StringUtil.concat("{success:", Boolean.toString(successful), ",value:",
                        StringUtil.quote(StringUtil.convertHTML(obj)), "}"));
    }
}
