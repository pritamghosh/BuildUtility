package com.project.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ProjectsHolder")
public class ProjectHolder {
    private Map<String, ProjectDO> map = new LinkedHashMap<>();

    @XmlElement(name = "Projects", type = ProjectDO.class)
    public final Map<String, ProjectDO> getMap() {
        return map;
    }

    public final void setMap(Map<String, ProjectDO> map) {
        this.map = map;
    }
}
