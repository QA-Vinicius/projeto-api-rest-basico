package cursoApi.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="user") 			// Nomeia a tag superior do XML
@XmlAccessorType(XmlAccessType.FIELD)	// Pega todos os atributos que a classe tiver, inclusive os que não estiverem cobertos por Getters/Setters
public class User {
	
	private String name;
	private Integer age;
	@XmlAttribute
	private Long id;
	private Double salary;

	public User() {
	}
	
	public User(String name, Integer age) {
		super();
		this.name = name;
		this.age = age;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getAge() {
		return age;
	}
	
	public void setAge(Integer age) {
		this.age = age;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Double getSalary() {
		return salary;
	}
	
	public void setSalary(Double salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", age=" + age + ", id=" + id + ", salary=" + salary + "]";
	}	
}