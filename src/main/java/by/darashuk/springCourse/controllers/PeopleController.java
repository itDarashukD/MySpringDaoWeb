package by.darashuk.springCourse.controllers;


import by.darashuk.springCourse.dao.PersonDAO;
import by.darashuk.springCourse.models.Person;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personDAO.show(id));
        return "people/show";
    }


    @GetMapping("/new")                         //при получении в запросе new - создаем новый обхект Person,
    public String newPeople(@ModelAttribute("person") Person person) {
        return "people/new";                   //передаем его в представление в папке people-new
    }

    @PostMapping
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {//создан объект персон, проинициализирован
        if(bindingResult.hasErrors()){return "people/new";}    //внедрили прямо в метод проверку на вилидность занчений з класса Персон, если значения не валидны - сраазу переходим на страницу создания нового человека
        //setName,aetAge и др.
        personDAO.save(person);              //вызван метод добавления в список

        return "redirect:/people";//сдесь сделан редирект на другую страницу
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personDAO.show(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person,BindingResult bindingResult,
                         @PathVariable("id") int id) {
       if(bindingResult.hasErrors()){return "people/edit";}
        personDAO.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public  String  delete(@PathVariable("id") int id){
        personDAO.delete(id);

        return "redirect:/people";

    }

}