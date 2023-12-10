//package com.example.utils;
//
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
//import java.io.IOException;
//
//public class testJavaWeb {
//    @WebFilter(filterName = "SecurityCheck", value = "/securitycheck/*")
//    public class SecurityCheck implements Filter {
//        public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//            HttpServletRequest request = (HttpServletRequest) req;
//            HttpServletResponse response = (HttpServletResponse) res;
//            HttpSession session = request.getSession();
//            String username = (String) session.getAttribute("name");
//            //条件成立时，不需要过滤,进入下个过滤或到servlet
//            if (username != null) {
//                //System.out.println("filter suc");
//                chain.doFilter(req, res);
//            } else response.sendRedirect("/Filter/bookmain.html");
//        }
//    }
//}