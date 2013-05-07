package com.sa.rest;

import java.io.IOException;
import javax.servlet.http.*;

import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
import com.sa.dao.TwizzerDao;

@SuppressWarnings("serial")
public class TwizzerServServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json");
		String location = req.getParameter("location");
		String search = req.getParameter("search");
		double radius = Double.parseDouble(req.getParameter("radius"));
		 
		Gson gson = new Gson(); 
		
		location = location.replaceAll("\\s", "+");
		
		TwizzerDao dao = new TwizzerDao();
		JSONObject obj = new JSONObject();
		String json = gson.toJson(dao.getSearchedResult(location, search, radius));
		
		resp.getWriter().print(json);
		
	}
}
