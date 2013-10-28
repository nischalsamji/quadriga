/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.spring.quadriga.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Karthik
 */
@Entity
@Table(name = "tbl_workspace")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WorkspaceDTO.findAll", query = "SELECT w FROM WorkspaceDTO w"),
    @NamedQuery(name = "WorkspaceDTO.findByWorkspacename", query = "SELECT w FROM WorkspaceDTO w WHERE w.workspacename = :workspacename"),
    @NamedQuery(name = "WorkspaceDTO.findByWorkspaceid", query = "SELECT w FROM WorkspaceDTO w WHERE w.workspaceid = :workspaceid"),
    @NamedQuery(name = "WorkspaceDTO.findByIsarchived", query = "SELECT w FROM WorkspaceDTO w WHERE w.isarchived = :isarchived"),
    @NamedQuery(name = "WorkspaceDTO.findByIsdeactivated", query = "SELECT w FROM WorkspaceDTO w WHERE w.isdeactivated = :isdeactivated"),
    @NamedQuery(name = "WorkspaceDTO.findByUpdatedby", query = "SELECT w FROM WorkspaceDTO w WHERE w.updatedby = :updatedby"),
    @NamedQuery(name = "WorkspaceDTO.findByUpdateddate", query = "SELECT w FROM WorkspaceDTO w WHERE w.updateddate = :updateddate"),
    @NamedQuery(name = "WorkspaceDTO.findByCreatedby", query = "SELECT w FROM WorkspaceDTO w WHERE w.createdby = :createdby"),
    @NamedQuery(name = "WorkspaceDTO.findByCreateddate", query = "SELECT w FROM WorkspaceDTO w WHERE w.createddate = :createddate")})
public class WorkspaceDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "workspacename")
    private String workspacename;
    @Lob
    @Column(name = "description")
    private String description;
    @Id
    @Basic(optional = false)
    @Column(name = "workspaceid")
    private String workspaceid;
    @Basic(optional = false)
    @Column(name = "isarchived")
    private short isarchived;
    @Basic(optional = false)
    @Column(name = "isdeactivated")
    private short isdeactivated;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workspaceDTO")
    private List<WorkspaceDspaceDTO> workspaceDspaceDTOList;
    @JoinColumn(name = "workspaceowner", referencedColumnName = "username")
    @ManyToOne(optional = false)
    private QuadrigaUserDTO workspaceowner;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workspaceDTO")
    private List<WorkspaceCollaboratorDTO> workspaceCollaboratorDTOList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workspaceDTO")
    private List<ProjectWorkspaceDTO> projectWorkspaceDTOList;

    public WorkspaceDTO() {
    }

    public WorkspaceDTO(String workspaceid) {
        this.workspaceid = workspaceid;
    }

    public WorkspaceDTO(String workspaceid, String workspacename, short isarchived, short isdeactivated, String updatedby, Date updateddate, String createdby, Date createddate) {
        this.workspaceid = workspaceid;
        this.workspacename = workspacename;
        this.isarchived = isarchived;
        this.isdeactivated = isdeactivated;
        this.updatedby = updatedby;
        this.updateddate = updateddate;
        this.createdby = createdby;
        this.createddate = createddate;
    }

    public String getWorkspacename() {
        return workspacename;
    }

    public void setWorkspacename(String workspacename) {
        this.workspacename = workspacename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWorkspaceid() {
        return workspaceid;
    }

    public void setWorkspaceid(String workspaceid) {
        this.workspaceid = workspaceid;
    }

    public short getIsarchived() {
        return isarchived;
    }

    public void setIsarchived(short isarchived) {
        this.isarchived = isarchived;
    }

    public short getIsdeactivated() {
        return isdeactivated;
    }

    public void setIsdeactivated(short isdeactivated) {
        this.isdeactivated = isdeactivated;
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

    @XmlTransient
    public List<WorkspaceDspaceDTO> getWorkspaceDspaceDTOList() {
        return workspaceDspaceDTOList;
    }

    public void setWorkspaceDspaceDTOList(List<WorkspaceDspaceDTO> workspaceDspaceDTOList) {
        this.workspaceDspaceDTOList = workspaceDspaceDTOList;
    }

    public QuadrigaUserDTO getWorkspaceowner() {
        return workspaceowner;
    }

    public void setWorkspaceowner(QuadrigaUserDTO workspaceowner) {
        this.workspaceowner = workspaceowner;
    }

    @XmlTransient
    public List<WorkspaceCollaboratorDTO> getWorkspaceCollaboratorDTOList() {
        return workspaceCollaboratorDTOList;
    }

    public void setWorkspaceCollaboratorDTOList(List<WorkspaceCollaboratorDTO> workspaceCollaboratorDTOList) {
        this.workspaceCollaboratorDTOList = workspaceCollaboratorDTOList;
    }

    @XmlTransient
    public List<ProjectWorkspaceDTO> getProjectWorkspaceDTOList() {
        return projectWorkspaceDTOList;
    }

    public void setProjectWorkspaceDTOList(List<ProjectWorkspaceDTO> projectWorkspaceDTOList) {
        this.projectWorkspaceDTOList = projectWorkspaceDTOList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (workspaceid != null ? workspaceid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WorkspaceDTO)) {
            return false;
        }
        WorkspaceDTO other = (WorkspaceDTO) object;
        if ((this.workspaceid == null && other.workspaceid != null) || (this.workspaceid != null && !this.workspaceid.equals(other.workspaceid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "hpsdtogeneration.WorkspaceDTO[ workspaceid=" + workspaceid + " ]";
    }
    
}