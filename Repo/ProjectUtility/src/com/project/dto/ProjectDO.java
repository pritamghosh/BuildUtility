package com.project.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author M1034079
 *
 */
@XmlRootElement(name = "Project", namespace = "http://com.project.dto")
public class ProjectDO implements Serializable {
    private static final long serialVersionUID = 3339582449509870731L;
    private String projectName;
    private String path;
    private String parentId;
    private Map<String, ProjectDO> modules = new HashMap<>(1);

    @XmlAttribute(name = "ProjectName")
    public final String getProjectName() {
        return projectName;
    }

    public final void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @XmlElement(name = "Modules", type = ProjectDO.class)
    public final Map<String, ProjectDO> getModules() {
        return modules;
    }

    @XmlElement(name = "Path")
    public final String getPath() {
        return path;
    }

    public final void setPath(String path) {
        this.path = path;
    }

    public final void setModules(Map<String, ProjectDO> modules) {
        this.modules = modules;
    }

    @XmlAttribute(name = "ParentId")
    public final String getParentId() {
        return parentId;
    }

    public final void setParentId(String parentId) {
        this.parentId = parentId;
    }


    @Override
    public String toString() {
        return " [ " + projectName + path + parentId + "]";
    }

}
