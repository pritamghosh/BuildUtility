package com.project.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <pre>
 * <b>Description : </b>
 * ResourceHolder.
 * 
 * @version $Revision: 1 $ $Date: Oct 16, 2017 8:20:18 PM $
 * @author $Author: pritam.ghosh $ 
 * </pre>
 */
@XmlRootElement(name = "ResourcesHolder")
public class ResourceHolder {
    private List<ProjectDO> resources = new ArrayList<>();
    private Map<String, String> filters = new TreeMap<>();

    @XmlElement(name = "Resources", type = ProjectDO.class)
    public final List<ProjectDO> getResources() {
        return resources;
    }

    public final void setResources(List<ProjectDO> resources) {
        this.resources = resources;
    }

    @XmlElement(name = "Filters")
    public final Map<String, String> getFilters() {
        return filters;
    }

    public final void setFilters(Map<String, String> filters) {
        this.filters = filters;
    }

}
