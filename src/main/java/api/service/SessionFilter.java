package api.service;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by yotam on 10/01/2016.
 */
public class SessionFilter implements Filter {
    private ArrayList<String> urlList;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        urlList = new ArrayList<>();
        String urls = filterConfig.getInitParameter("avoid-urls");
        StringTokenizer token = new StringTokenizer(urls, ",");
        while (token.hasMoreTokens()) {
            urlList.add(token.nextToken());
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String sessionExpired = ""; // web page that will be sent when session is not valid

        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            String url = httpServletRequest.getServletPath();
            boolean allowedRequest = false;

            for (String allow : urlList) {
                if (url.contains(allow)) {
                    allowedRequest = true;
                    break;
                }
            }

            if (!allowedRequest) {
                HttpSession session = httpServletRequest.getSession(false);
                if (session == null) {
                    httpServletRequest.getRequestDispatcher(sessionExpired).forward(request, response);
                } else {
                    Integer userID = SessionService.getUserIdFromStoredSession();
                    if (userID == null) {
                        httpServletResponse.sendRedirect(sessionExpired);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
