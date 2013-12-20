package edu.asu.spring.quadriga.dspace.service.impl;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import edu.asu.spring.quadriga.dspace.service.IDspaceMetadataCollection;
import edu.asu.spring.quadriga.dspace.service.IDspaceMetadataItemEntity;


@XmlRootElement(name="itementity")
public class DspaceMetadataItemEntity implements IDspaceMetadataItemEntity {

	private String id;
	private String handle;
	private String name;
	
	private IDspaceMetadataCollection collections;
	
	@Override
	public String getId() {
		return id;
	}
	@Override
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String getHandle() {
		return handle;
	}
	@Override
	public void setHandle(String handle) {
		this.handle = handle;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElementRefs({@XmlElementRef(type=DspaceMetadataCollection.class)})
	@Override
	public IDspaceMetadataCollection getCollections() {
		return collections;
	}
	@Override
	public void setCollections(IDspaceMetadataCollection collections) {
		this.collections = collections;
	}


	public static class Adapter extends XmlAdapter<DspaceMetadataItemEntity, IDspaceMetadataItemEntity>
	{
		@Override
		public IDspaceMetadataItemEntity unmarshal(DspaceMetadataItemEntity v) throws Exception {
			return v;
		}

		@Override
		public DspaceMetadataItemEntity marshal(IDspaceMetadataItemEntity v) throws Exception {
			return (DspaceMetadataItemEntity)v;
		}
	}
}
