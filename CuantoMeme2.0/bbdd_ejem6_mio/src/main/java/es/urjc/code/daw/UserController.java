package es.urjc.code.daw;

import java.security.Principal;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.urjc.code.daw.comentario.Comentario;
import es.urjc.code.daw.comentario.ComentarioRepository;
import es.urjc.code.daw.tag.Tag;
import es.urjc.code.daw.tag.TagRepository;
import es.urjc.code.daw.user.User;
import es.urjc.code.daw.user.UserRepository;
import es.urjc.code.daw.vineta.Vineta;
import es.urjc.code.daw.vineta.VinetaRepository;
import es.urjc.code.daw.user.*;

@Controller
public class UserController {

	@Autowired
	private UserRepository userrepository;

	@Autowired
	private ComentarioRepository comentariorepository;
	
	@Autowired
	private VinetaRepository vinetarepository;
	
	@Autowired
	private TagRepository tagrepository;
	
	@Autowired
	private UserComponent userComponent;

	@PostConstruct
	public void init(){
		
		User usuario3 = new User("pepe", "pepito", "cuantomeme3@gmail.com", "ROLE_USER");
		User usuario4 = new User("jose", "josito", "cuantomeme4@gmail.com", "ROLE_USER");
		
		Vineta v3 = new Vineta("vineta3", "des3", "http://i2.kym-cdn.com/photos/images/facebook/000/125/918/RMUBQ.png");
		Vineta v4 = new Vineta("vineta4", "des4", "http://i0.kym-cdn.com/photos/images/newsfeed/000/125/163/ragek.jpg?1318992465");
		
		v3.setAutor(usuario3);
		v4.setAutor(usuario4);
		
		this.userrepository.save(usuario3);
		this.userrepository.save(usuario4);
		this.vinetarepository.save(v3);
		this.vinetarepository.save(v4);
		
		Tag t4 = new Tag("Trolldad");
		Tag t5 = new Tag("Inglip");
		Tag t6 = new Tag("Yaoming");
		
		this.tagrepository.save(t4);
		this.tagrepository.save(t5);
		this.tagrepository.save(t6);
		
		v3.getTags().add(t4);
		v3.getTags().add(t5);
		v4.getTags().add(t4);
		v4.getTags().add(t6);
		
		this.vinetarepository.save(v3);
		this.vinetarepository.save(v4);
		
		Comentario c2 = new Comentario("pole");
		c2.setAutor_comentario(usuario3);
		c2.setVineta(v3);
		this.comentariorepository.save(c2);
	}
	/*--------------------------Autenticacion--------------------------*/
	@RequestMapping("/login")
	public String login() {		
		return "login";
	}
	
	@RequestMapping("/loginerror")
	public String loginError() {		
		return "loginerror";
	}
	@RequestMapping("/logout")
	public String logout() {		
		return "index";
	}
	@RequestMapping("/signup")
	public String signup() {		
		return "signup";
	}
	@RequestMapping("/home")
	public String profile(Model model, HttpServletRequest request) {
		Principal p = request.getUserPrincipal();
    	User user = userrepository.findByUsername(p.getName());
    	System.out.println(userComponent.isLoggedUser());
		model.addAttribute("anonymous", !userComponent.isLoggedUser());
		//model.addAttribute("usuario_logged", user);    
		model.addAttribute("usuario", user);
		return "perfil";
	}
	
	@RequestMapping(value = "/signupuser", method = RequestMethod.POST)
	public String login(Model model, HttpSession sesion, @RequestParam String username, @RequestParam String password, @RequestParam String email ) {
		User usuario = new User(username, password, email,"ROLE_USER" );
		this.userrepository.save(usuario);
		return "redirect:/";		
	}

	@RequestMapping("/perfil/{id}")
	public String perfil(Model model, @PathVariable long id, HttpServletRequest request) {
		  User usuario = this.userrepository.findOne(id);
		  model.addAttribute("usuario", usuario);
		  model.addAttribute("anonymous", !userComponent.isLoggedUser());
		  /*
		  if (userComponent.isLoggedUser()){
			  Principal p = request.getUserPrincipal();
		      User user = userrepository.findByUsername(p.getName());
		      model.addAttribute("usuario_logged", user);
		  }else{
			  model.addAttribute("usuario_logged", new User("por defecto", "pass", "asd", "Role_User"));
		  }
		   */
		  
		return "perfil";
	}
	/*------------------Comentarios-------------------------*/
	@RequestMapping(value = "/crearComentario/vineta/{id}", method = RequestMethod.POST)
	public String crearComentario(Model model, HttpSession sesion,@PathVariable long id, @RequestParam String comentario, HttpServletRequest request ) {
		System.out.println("he entrado a crear un comentario");
		if (userComponent.isLoggedUser()){
			  Principal p = request.getUserPrincipal();
		      User user = userrepository.findByUsername(p.getName());
		      Comentario c = new Comentario(comentario);
		      c.setAutor_comentario(user);
		      c.setVineta(this.vinetarepository.findOne(id));
		      this.comentariorepository.save(c);
		      return "redirect:/vineta/"+id;
			
		}else{
			return "redirect:/login";
		}

	}
	
	/*------------------Vinetas-------------------------*/
	@RequestMapping("/vineta/{id}")
	public String detalles(Model model, @PathVariable long id, HttpServletRequest request) {
		model.addAttribute("vineta", this.vinetarepository.findOne((long) id));
		model.addAttribute("anonymous", !userComponent.isLoggedUser());
		if (userComponent.isLoggedUser()){
			Principal p = request.getUserPrincipal();
	    	User usuario = userrepository.findByUsername(p.getName());
			model.addAttribute("usuario", usuario);
		}
		return "detalles";
	}
	
	@RequestMapping(value = "/likevineta/{id}")
	public String likeVineta(Model model, @PathVariable long id) {
		Vineta vineta = this.vinetarepository.findOne(id);
		vineta.like();
		this.vinetarepository.save(vineta);
		return "redirect:/vineta/"+id;
	}
	
	@RequestMapping(value = "/dislikevineta/{id}")
	public String dislikeVineta(Model model, @PathVariable long id) {
		Vineta vineta = this.vinetarepository.findOne(id);
		vineta.dislike();
		this.vinetarepository.save(vineta);
		return "redirect:/vineta/"+id;
	}
	
	
	
	/*----------------------------s-------------------------*/
	
	@RequestMapping("/")
	public String viñetas(Model model, HttpServletRequest request) {
		model.addAttribute("anonymous", !userComponent.isLoggedUser());
		if (userComponent.isLoggedUser()){
			Principal p = request.getUserPrincipal();
	    	User usuario = userrepository.findByUsername(p.getName());
			model.addAttribute("usuario", usuario);
		}
		model.addAttribute("vinetas", this.vinetarepository.findAll());
		model.addAttribute("tags_mas_usados", this.tagrepository.findAll());
		return "index";
	}

	
	@RequestMapping("/tag/{nombre}")
	public String detalles(Model model, @PathVariable String nombre, HttpServletRequest request) {
		model.addAttribute("anonymous", !userComponent.isLoggedUser());
		if (userComponent.isLoggedUser()){
			Principal p = request.getUserPrincipal();
	    	User usuario = userrepository.findByUsername(p.getName());
			model.addAttribute("usuario", usuario);
		}
		model.addAttribute("tag",this.tagrepository.findByNombre(nombre));
		model.addAttribute("lista", this.tagrepository.findByNombre(nombre).getVinetas());
		return "tagIndex";
	}
	

	
	
	@RequestMapping("/vinetas")
	public String vinetas(Model model) {
		model.addAttribute("vinetas", this.vinetarepository.findAll());
		return "index";
	}

}