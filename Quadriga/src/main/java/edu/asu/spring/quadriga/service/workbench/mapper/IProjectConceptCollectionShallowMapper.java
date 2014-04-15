package edu.asu.spring.quadriga.service.workbench.mapper;

import java.util.List;

import edu.asu.spring.quadriga.domain.workbench.IProject;
import edu.asu.spring.quadriga.domain.workbench.IProjectConceptCollection;
import edu.asu.spring.quadriga.dto.ProjectDTO;
import edu.asu.spring.quadriga.exceptions.QuadrigaStorageException;

public interface IProjectConceptCollectionShallowMapper {
	public List<IProjectConceptCollection> getProjectConceptCollectionList(IProject project,ProjectDTO projectDTO) throws QuadrigaStorageException;

}
