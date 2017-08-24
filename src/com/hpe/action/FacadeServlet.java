package com.hpe.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import com.hpe.base.annotation.RequestMapping;
import com.hpe.base.entity.Action;


/**
 * Servlet implementation class FacadeServlet
 */

@Action()
@RequestMapping("/hpe/FacadeServlet")
@Controller
public class FacadeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
