package com.pvmsys.brix.graph.main;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pvmsys.brix.graph.graphServlet.GenericCanvasServlet;

@Controller
public class ControllerRegistration
{

	
	@GetMapping("/login")
	public String login()
	{
		return "index";
	}
	
	@GetMapping("/brixsearch")
	public String index()
	{
		return "brixsearch";
	}
	
	@RequestMapping("/measurementview")
	public String getNewTab(){
		return "measurementview";
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public ServletRegistrationBean servletRegistrationBean(){
	    return new ServletRegistrationBean(new LoginServlet(),"/LoginServlet/*");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public ServletRegistrationBean servletRegistrationForCanvas(){
	    return new ServletRegistrationBean(new GenericCanvasServlet(),"/gsearch/*");
	}
	
	@PostMapping("/hello")
	public String sayHello(@RequestParam("name") String name, Model model)
	{
		model.addAttribute("name",name);
		return "hello";
	}
	
}
