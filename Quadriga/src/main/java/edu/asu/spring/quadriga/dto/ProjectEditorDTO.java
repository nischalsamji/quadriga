/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.spring.quadriga.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents the column mappings for project editor table.
 * @author Karthik
 */
@Entity
@Table(name = "tbl_project_editor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProjectEditorDTO.findAll", query = "SELECT p FROM ProjectEditorDTO p"),
    @NamedQuery(name = "ProjectEditorDTO.findByProjectid", query = "SELECT p FROM ProjectEditorDTO p WHERE p.projectEditorDTOPK.projectid = :projectid"),
    @NamedQuery(name = "ProjectEditorDTO.findByEditor",query = "SELECT p FROM ProjectEditorDTO p WHERE p.projectEditorDTOPK.editor = :editor")
    })

public class ProjectEditorDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProjectEditorDTOPK projectEditorDTOPK;
    @JoinColumn(name = "projectid" , referencedColumnName = "projectid" ,insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ProjectDTO project;

	@JoinColumn(name = "editor" , referencedColumnName = "username" , insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private QuadrigaUserDTO quadrigaUserDTO;
    @Basic(optional = false)
    @Column(name = "updatedby")
    private String updatedby;
    @Basic(optional = false)
    @Column(name = "updateddate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateddate;
    @Basic(optional = false)
    @Column(name = "createdby")
    private String createdby;
    @Basic(optional = false)
    @Column(name = "createddate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createddate;

    public ProjectEditorDTO() {
    }

    public ProjectEditorDTO(ProjectEditorDTOPK projectEditorDTOPK, String updatedby, Date updateddate, String createdby, Date createddate) {
        this.projectEditorDTOPK = projectEditorDTOPK;
        this.updatedby = updatedby;
        this.updateddate = updateddate;
        this.createdby = createdby;
        this.createddate = createddate;
    }

    public ProjectEditorDTO(String projectid, String editor, String updatedby, Date updateddate, String createdby, Date createddate) {
        this.projectEditorDTOPK = new ProjectEditorDTOPK(projectid, editor);
        this.createdby = createdby;
        this.createddate = createddate;
        this.updatedby = updatedby;
        this.updateddate = updateddate;
    }

    public ProjectEditorDTOPK getProjectEditorDTOPK() {
        return projectEditorDTOPK;
    }

    public void setProjectEditorDTOPK(ProjectEditorDTOPK projectEditorDTOPK) {
        this.projectEditorDTOPK = projectEditorDTOPK;
    }
    
    public ProjectDTO getProject() {
		return project;
	}

	public void setProject(ProjectDTO project) {
		this.project = project;
	}


    public QuadrigaUserDTO getQuadrigaUserDTO() {
		return quadrigaUserDTO;
	}

	public void setQuadrigaUserDTO(QuadrigaUserDTO quadrigaUserDTO) {
		this.quadrigaUserDTO = quadrigaUserDTO;
	}

	public String getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(String updatedby) {
        this.updatedby = updatedby;
    }

    public Date getUpdateddate() {
        return updateddate;
    }

    public void setUpdateddate(Date updateddate) {
        this.updateddate = updateddate;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public Date getCreateddate() {
        return createddate;
    }

    public void setCreateddate(Date createddate) {
        this.createddate = createddate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (projectEditorDTOPK != null ? projectEditorDTOPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProjectEditorDTO)) {
            return false;
        }
        ProjectEditorDTO other = (ProjectEditorDTO) object;
        if ((this.projectEditorDTOPK == null && other.projectEditorDTOPK != null) || (this.projectEditorDTOPK != null && !this.projectEditorDTOPK.equals(other.projectEditorDTOPK))) {
            return false;
        }
        return true;
    }
}
