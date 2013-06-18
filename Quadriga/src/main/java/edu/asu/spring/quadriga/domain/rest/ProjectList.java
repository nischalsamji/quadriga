package edu.asu.spring.quadriga.domain.rest;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import edu.asu.spring.quadriga.domain.IProject;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
 "project"
})
@XmlRootElement(name = "projectList")
public class ProjectList {
	 
	 @XmlElement(required = true, namespace="http://www.digitalhps.org/")
	 protected List<IProject> projectList;
	 
	 public List<IProject> getProjectList() {
	     return this.projectList;
	 }
	 public void SetProjectList(List<IProject> projectList) {
	     this.projectList=projectList;
	 }
}
